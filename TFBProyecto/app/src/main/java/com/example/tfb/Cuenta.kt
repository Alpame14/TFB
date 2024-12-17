package com.example.tfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tfb.databinding.ActivityCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
class Cuenta : AppCompatActivity() {
    private lateinit var binding: ActivityCuentaBinding
    private lateinit var storageFoto: StorageFoto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar StorageFoto y pasar el contexto y el ImageView
        storageFoto = StorageFoto(this, binding.imageView)

        // Manejar el diseño
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mostrar información del usuario
        binding.usuariotxt.text = Usuario.currentUsuario?.nombre ?: ""
        binding.emailtxt.text = Usuario.currentUsuario?.email ?: ""
        binding.scoretxt.text = Usuario.currentUsuario?.maxscore?.toString() ?: ""

        // Botón de cerrar sesión
        binding.logoutbtn.setOnClickListener {
            cerrarSesion()
        }

        // Al hacer clic en el ImageView, abre la galería
        binding.imageView.setOnClickListener {
            storageFoto.abrirGaleria(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Delegar el resultado a StorageFoto
        storageFoto.manejarResultado(requestCode, resultCode, data)
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val prefs: SharedPreferences.Editor =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
