package com.example.tfb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityRegistroBinding


class Registro : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando view binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acciones en los botones
        binding.btRegistrar.setOnClickListener {
            // Lógica para registrar al usuario
        }

        binding.btRegistrar2.setOnClickListener {
            // Lógica para iniciar sesión
        }
    }
}