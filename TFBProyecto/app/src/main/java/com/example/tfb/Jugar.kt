package com.example.tfb

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfb.AdapterCom.JuegoAdapter
import com.example.tfb.AdapterCom.ComidaProvider.Companion.listaComida
import com.example.tfb.databinding.ActivityJugarBinding
import kotlin.random.Random
import com.example.tfb.Enumerados.*

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding
    private lateinit var adapter: JuegoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJugarBinding.inflate(layoutInflater)
        val platos = listOf(
            binding.imPlato1,
            binding.imPlato2,
            binding.imPlato3,
            binding.imPlato4
        )
        setContentView(binding.root)
        binding.btInicio.isEnabled = true

        reiniciaTablero()

        // Configurar el botón para iniciar el cronómetro
        binding.btInicio.setOnClickListener {
            startCountdownTimer()
            binding.btInicio.isEnabled = false
            binding.btInicio.alpha = 0.5f

            juego()
        }


        iniciaRecycler(listaComida)
        // Configura el adaptador con un listener para actualizar las imágenes
        adapter = JuegoAdapter(listaComida) { comida ->
            // Actualiza las imágenes con el objeto seleccionado
            if (comida.bebida) {
                binding.ivBebida.setImageResource(comida.foto)
            } else {
                val platos = listOf(binding.imPlato1, binding.imPlato2, binding.imPlato3, binding.imPlato4)
                for (plato in platos) {
                    if (plato.drawable == null) {
                        plato.setImageResource(comida.foto)
                        break
                    }
                }
            }

            // Asignar onClickListener a cada plato
            for (plato in platos) {
                plato.setOnClickListener {
                    // Eliminar la imagen del plato al hacer clic
                    plato.setImageResource(0)
                    Toast.makeText(this, "Plato vaciado", Toast.LENGTH_SHORT).show()
                }
            }
            binding.ivBebida.setOnClickListener {
                // Eliminar la imagen de la bebida al hacer clic
                binding.ivBebida.setImageResource(0)
                Toast.makeText(this, "Bebida vaciada", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Seleccionaste: ${comida.nombre}", Toast.LENGTH_SHORT).show()
        }
        binding.rvJuego.adapter = adapter

        binding.enviarbtn.setOnClickListener {


            generaCliente()
            reiniciaTablero()

        }

    }

    private fun juego() {

    }

    // Función para generar un cliente aleatorio
    fun generaCliente(): Cliente {
        /*

         posiblemente esto tendria que meterse en firebase
         */
        val nombres = listOf(
            "Carlos", "Ana", "Pedro", "Lucía", "María", "Luis", "Elena", "Javier",
            "Sofía", "Manuel", "Isabel", "David", "Carmen", "Pablo", "Paula", "Sergio",
            "Marta", "Adrián", "Raquel", "Daniel", "Alba", "Jorge", "Cristina", "Álvaro",
            "Laura", "Rubén", "Patricia", "Diego", "Natalia", "Víctor", "Sara", "Guillermo",
            "Andrea", "Ricardo", "Beatriz", "José", "Silvia", "Fernando")

        val nombreAleatorio = nombres.random() // Elegir un nombre aleatorio del array
        val alergenoAleatorio = Alergeno.values().random() // Elegir un alérgeno aleatorio del enum
        val dietaAleatoria = Dietas.values().random() // Elegir una dieta aleatoria del enum

        val fotoAleatoria = Random.nextInt(1, 100) // ID ficticio de drawable

        return Cliente(
            nombre = nombreAleatorio,
            alergeno = alergenoAleatorio,
            dieta = dietaAleatoria,
            foto = fotoAleatoria
        )
    }

    private fun reiniciaTablero() {
        binding.ivBebida.setImageResource(0) // Limpia la imagen
        binding.imPlato1.setImageResource(0)
        binding.imPlato2.setImageResource(0)
        binding.imPlato3.setImageResource(0)
        binding.imPlato4.setImageResource(0)
    }



    private fun iniciaRecycler(lista: List<Comida>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvJuego.layoutManager = layoutManager
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
