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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando View Binding
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
                                val userMap = mapOf(
                                    "username" to usernameText,
                                    "email" to emailText
                                )

                                FirebaseFirestore.getInstance().collection("users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        // Guardar en SharedPreferences
                                        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                                        prefs.putString("email", emailText)
                                        prefs.putString("provider", ProviderType.BASIC.name)
                                        prefs.putString("nombre", usernameText)
                                        prefs.apply()

                                        Usuario.currentUsuario = Usuario.crearUsuario(
                                            nombre = usernameText,
                                            email = emailText,
                                            provider = ProviderType.BASIC,
                                            maxscore = 0
                                        )

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

        layout.addView(email)
        layout.addView(cont)

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
                                // Guardar en SharedPreferences
                                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                                prefs.putString("email", emailText)
                                prefs.putString("provider", ProviderType.BASIC.name)
                                prefs.apply()

                                Usuario.currentUsuario = Usuario.crearUsuario(
                                    nombre = "Nombre de usuario", // Cambiar si tienes el nombre
                                    email = emailText,
                                    provider = ProviderType.BASIC,
                                    maxscore = 0
                                )

                                showHome(emailText, ProviderType.BASIC)
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

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}

