package com.example.tfb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityRegistroBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class Registro : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    companion object {
        const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando view binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acciones en los botones
        binding.btRegistrar.setOnClickListener {
            popup_regis()

        }

        binding.btIniciarSesion.setOnClickListener {

            popup_login()
        }
    }

    private fun popup_regis() {
        // Crear el diseño
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        // Campo para el nombre de usuario
        val username = EditText(this).apply {
            hint = "Nombre de usuario"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        // Campo para el correo electrónico
        val email = EditText(this).apply {
            hint = "Correo electrónico"
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        // Campo para la contraseña
        val cont = EditText(this).apply {
            hint = "Contraseña"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // Agregar los campos al diseño
        layout.addView(username)
        layout.addView(email)
        layout.addView(cont)

        // Crear y mostrar el AlertDialog
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
                                // Guardar el nombre de usuario en Firestore o Realtime Database
                                val userId = task.result?.user?.uid ?: ""
                                val userMap = mapOf(
                                    "username" to usernameText,
                                    "email" to emailText
                                )

                                // Usa Firestore para almacenar los datos
                                FirebaseFirestore.getInstance().collection("users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        showHome(emailText, ProviderType.BASIC)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
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
            .setNeutralButton("Boton de entrar con google") { _, _ ->

            }
            .show()
    }



    private fun popup_login() {
        // Crear el diseño
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

        layout.addView(email)
        layout.addView(cont)

        // Crear y mostrar el AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Iniciar sesión")
        builder.setMessage("Ingresa tu correo y contraseña")
            .setView(layout)
            .setPositiveButton("Iniciar sesión") { _, _ ->
                val emailText = email.text.toString().trim()
                val contText = cont.text.toString().trim()

                if (emailText.isNotEmpty() && contText.isNotEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, contText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showHome(task.result?.user?.email ?: "", ProviderType.BASIC)
                            } else {
                                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Boton de entrar con google") { _, _ ->
                /*val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken()
                val googleSignInClient = GoogleSignIn.getClient(
                    this,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.web_client_id))
                        .requestEmail()
                        .build()
                )

                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
*/
            }
            .show()
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Error al iniciar sesión con Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showHome(task.result?.user?.email ?: "", ProviderType.GOOGLE)
                } else {
                    Toast.makeText(this, "Error al autenticar con Google: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun showHome(email: String, provider: ProviderType, username: String = "Invitado") {
        // Guardar en SharedPreferences
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider.name)
        prefs.putString("nombre", username) // Guarda el nombre correctamente
        prefs.apply()

        // Navegar a MainActivity
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
        finish()
    }



}