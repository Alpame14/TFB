package com.example.tfb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityRegistroBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

import com.example.tfb.Enumerados.ProviderType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Registro : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private val SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btRegistrar.setOnClickListener {
            popup_regis()
        }

        binding.btIniciarSesion.setOnClickListener {
            popup_login()
        }
    }

    private fun popup_regis() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val username = EditText(this).apply {
            hint = "Nombre de usuario"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val email = EditText(this).apply {
            hint = "Correo electrónico"
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        val cont = EditText(this).apply {
            hint = "Contraseña"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }


        layout.addView(username)
        layout.addView(email)
        layout.addView(cont)


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registrar")
            .setMessage("Ingresa tus datos para registrarte")
            .setView(layout)
            .setPositiveButton("Registrar") { _, _ ->
                val usernameText = username.text.toString().trim()
                val emailText = email.text.toString().trim()
                val contText = cont.text.toString().trim()

                if (usernameText.isNotEmpty() && emailText.isNotEmpty() && contText.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText, contText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = task.result?.user?.uid ?: ""
                                saveUserToFirestore(
                                    userId,
                                    usernameText,
                                    emailText,
                                    ProviderType.BASIC
                                )
                                val userMap = mapOf(
                                    "username" to usernameText,
                                    "email" to emailText
                                )

                                FirebaseFirestore.getInstance().collection("users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        val prefs = getSharedPreferences(
                                            getString(R.string.prefs_file),
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        prefs.putString("email", emailText)
                                        prefs.putString("provider", ProviderType.BASIC.name)
                                        prefs.putString("nombre", usernameText)
                                        prefs.apply()

                                        // Asignar los valores a Usuario.currentUsuario
                                        Usuario.currentUsuario = Usuario.crearUsuario(
                                            nombre = usernameText,
                                            email = emailText,
                                            provider = ProviderType.BASIC,
                                            maxscore = 0
                                        )

                                        showHome(emailText, ProviderType.BASIC)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Error al guardar: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun popup_login() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val email = EditText(this).apply {
            hint = "Correo electrónico"
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        val cont = EditText(this).apply {
            hint = "Contraseña"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val googleButton = Button(this).apply {
            text = "Iniciar sesión con Google"
            setOnClickListener {
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleClient = GoogleSignIn.getClient(context, googleConf)
                googleClient.signOut() // Se asegura de que el usuario se cierre antes de iniciar sesión
                startActivityForResult(googleClient.signInIntent, SIGN_IN)
            }
        }

        layout.addView(email)
        layout.addView(cont)
        layout.addView(googleButton)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Iniciar sesión")
            .setMessage("Ingresa tu correo y contraseña")
            .setView(layout)
            .setPositiveButton("Iniciar sesión") { _, _ ->
                val emailText = email.text.toString().trim()
                val contText = cont.text.toString().trim()

                if (emailText.isNotEmpty() && contText.isNotEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, contText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = task.result?.user?.uid ?: ""

                                // 🔹 Obtener los datos del usuario desde Firestore
                                FirebaseFirestore.getInstance().collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            val nombre = document.getString("username") ?: "Usuario sin nombre"
                                            val maxscore = document.getLong("maxscore")?.toInt() ?: 0

                                            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                                            prefs.putString("email", emailText)
                                            prefs.putString("provider", ProviderType.BASIC.name)
                                            prefs.putString("nombre", nombre)
                                            prefs.putInt("maxscore", maxscore)  // 🔹 Guardar maxscore en SharedPreferences
                                            prefs.apply()

                                            // 🔹 Asegurar que Usuario.currentUsuario se actualiza correctamente
                                            Usuario.currentUsuario = Usuario.crearUsuario(
                                                nombre = nombre,
                                                email = emailText,
                                                provider = ProviderType.BASIC,
                                                maxscore = maxscore
                                            )

                                            showHome(emailText, ProviderType.BASIC)
                                        } else {
                                            Toast.makeText(this, "No se encontró información del usuario.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al obtener datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = task.result?.user?.uid ?: ""

                            // 🔹 Obtener los datos del usuario desde Firestore
                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val nombre = document.getString("username") ?: "Usuario sin nombre"
                                        val maxscore = document.getLong("maxscore")?.toInt() ?: 0

                                        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                                        prefs.putString("email", account.email)
                                        prefs.putString("provider", ProviderType.GOOGLE.name)  // 🔹 Cambiar a Google
                                        prefs.putString("nombre", nombre)
                                        prefs.putInt("maxscore", maxscore)  // 🔹 Guardar maxscore en SharedPreferences
                                        prefs.apply()

                                        // 🔹 Asegurar que Usuario.currentUsuario se actualiza correctamente
                                        Usuario.currentUsuario = Usuario.crearUsuario(
                                            nombre = nombre,
                                            email = account.email ?: "email desconocido",
                                            provider = ProviderType.GOOGLE,
                                            maxscore = maxscore
                                        )

                                        showHome(account.email ?: "", ProviderType.GOOGLE)
                                    } else {
                                        Toast.makeText(this, "No se encontró información del usuario.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al obtener datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Error al iniciar sesión con Google: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error al intentar iniciar sesión con Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUserToFirestore(
        userId: String,
        username: String,
        email: String,
        provider: ProviderType
    ) {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

        userRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                // Si el usuario no existe, lo creamos con maxscore inicial en 0
                val userMap = mapOf(
                    "username" to username,
                    "email" to email,
                    "provider" to provider.name,
                    "maxscore" to 0  // Valor inicial de la puntuación
                )

                userRef.set(userMap)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Usuario guardado correctamente.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al guardar usuario: ${e.message}")
                    }
            }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error al comprobar usuario: ${e.message}")
        }
    }


    private fun showHome(email: String, provider: ProviderType) {
        // Lógica para llevar al usuario a la pantalla principal
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
        finish()  // Opcional, dependiendo de si quieres cerrar esta actividad
    }
}