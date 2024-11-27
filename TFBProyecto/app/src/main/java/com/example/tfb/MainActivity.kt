package com.example.tfb

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       
        // Cargar animaciones
        val scaleRotateIn = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_in)
        val scaleRotateOut = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_out)

        // Añadir clic y animación a ivPerfil
        binding.ivPerfil.setOnClickListener {
            // Inicia la animación
            it.startAnimation(scaleRotateIn)
            it.startAnimation(scaleRotateOut)

            // Espera a que termine la animación antes de iniciar la actividad
            it.postDelayed({
                val intent = Intent(this, Registro::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }

        // Añadir animaciones a las demás imágenes
        binding.ivAyuda.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Ayuda::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }

        binding.ivRanking.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Ranking::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }

        binding.ivJugar.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Jugar::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }
    }
}
// Esto es un cambio de prueba