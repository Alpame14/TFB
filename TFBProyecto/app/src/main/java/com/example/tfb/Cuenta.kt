package com.example.tfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tfb.databinding.ActivityCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class Cuenta : AppCompatActivity() {
    private lateinit var binding: ActivityCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando View Binding
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar el fondo dinámicamente según el idioma
        val currentLocale = Locale.getDefault().language
        binding.main.setBackgroundResource(
            if (currentLocale == "en") R.drawable.background_perfil_eng else R.drawable.background_perfil
        )

        // Manejo de insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Muestra los datos del usuario actual
        binding.usuariotxt.text = Usuario.currentUsuario?.nombre ?: ""
        binding.emailtxt.text = Usuario.currentUsuario?.email ?: ""
        binding.scoretxt.text = Usuario.currentUsuario?.maxscore?.toString() ?: ""

        // Acción de cerrar sesión
        binding.logoutbtn.setOnClickListener {
            // Cerrar sesión en Firebase
            FirebaseAuth.getInstance().signOut()
            // Borrar datos de SharedPreferences
            borraDatos()
            // Redirigir a la pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Cierra la actividad actual para que no regrese al hacer back
        }
    }

    private fun borraDatos() {
        // Borrar datos de SharedPreferences
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()  // Borra todas las preferencias
        prefs.apply()

        // Restaurar al usuario invitado
        Usuario.currentUsuario = Usuario.crearUsuarioInvitado()
    }
}
