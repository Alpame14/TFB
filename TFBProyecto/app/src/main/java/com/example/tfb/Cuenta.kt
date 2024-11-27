package com.example.tfb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tfb.databinding.ActivityCuentaBinding

class Cuenta : AppCompatActivity() {
    private lateinit var binding: ActivityCuentaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.usuariotxt.text = Usuario.currentUsuario?.nombre
        binding.emailtxt.text = Usuario.currentUsuario?.email
        binding.scoretxt.text = Usuario.currentUsuario?.maxscore.toString()


        binding.logoutbtn.setOnClickListener {
            borraDatos()


        }


    }


    private fun borraDatos() {
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        Usuario.crearUsuarioInvitado()
    }
}