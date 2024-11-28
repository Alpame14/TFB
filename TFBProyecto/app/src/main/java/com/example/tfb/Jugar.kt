package com.example.tfb

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityJugarBinding

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el botón para iniciar el cronómetro
        binding.btInicio.setOnClickListener {
            startCountdownTimer()
        }
    }

    private fun startCountdownTimer() {
        // Crear un cronómetro de cuenta atrás de 2 minutos
        val countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calcular minutos y segundos restantes
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60

                // Actualizar el TextView con el tiempo restante
                binding.tvCrono.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Cuando termine la cuenta atrás, mostrar 00:00
                binding.tvCrono.text = "00:00"
            }
        }

        // Iniciar el cronómetro
        countDownTimer.start()
    }
}
