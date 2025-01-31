package com.example.tfb

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfb.AdapterCom.ComidaProvider
import com.example.tfb.AdapterCom.JuegoAdapter
import com.example.tfb.databinding.ActivityJugarBinding
import com.example.tfb.Enumerados.*

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding
    private lateinit var adapter: JuegoAdapter
    private var platosMap: MutableList<Comida> = ComidaProvider.listaComida.toMutableList()
    private var copia = platosMap
    private var mediaPlayer: MediaPlayer? = null // ✅ MediaPlayer para la música

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val platos = listOf(
            binding.imPlato1,
            binding.imPlato2,
            binding.imPlato3,
            binding.imPlato4
        )

        binding.btInicio.isEnabled = true
        binding.enviarbtn.isEnabled = false
        binding.enviarbtn.alpha = 0.5f

        reiniciaTablero(platos)

        // ✅ Configurar el botón para iniciar el cronómetro y la música
        binding.btInicio.setOnClickListener {
            startCountdownTimer()
            binding.btInicio.isEnabled = false
            binding.btInicio.alpha = 0.5f
            binding.enviarbtn.isEnabled = true
            binding.enviarbtn.alpha = 1.0f

            juego(platos)

            // ✅ Iniciar la música cuando se pulse el botón (en bucle)
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cancion_juego)
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            }
        }

        binding.enviarbtn.setOnClickListener {
            calculapuntuacion()
            generaCliente()
            reiniciaTablero(platos)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ✅ Detener la música cuando se cierre la Activity
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun calculapuntuacion(): Int {
        return 0
    }

    private fun juego(platos: List<ImageView>) {
        generaCliente()
        iniciaRecycler()

        val listaComidas = platosMap.toMutableList()
        adapter = JuegoAdapter(listaComidas) { comida ->
            var comidaSeleccionada: Comida? = comida

            if (comida.bebida == true) {
                binding.ivBebida.setImageResource(comida.foto)
                binding.ivBebida.tag = comida
            } else {
                for (plato in platos) {
                    if (plato.drawable == null) {
                        plato.setImageResource(comida.foto)
                        plato.tag = comida
                        listaComidas.remove(comida)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }

            for (plato in platos) {
                plato.setOnClickListener {
                    val comidaADevolver = plato.tag as? Comida
                    if (comidaADevolver != null) {
                        listaComidas.add(comidaADevolver)
                        adapter.notifyDataSetChanged()
                    }
                    plato.setImageResource(0)
                    plato.tag = null
                    Toast.makeText(this, "Plato vaciado", Toast.LENGTH_SHORT).show()
                }
            }

            binding.ivBebida.setOnClickListener {
                val bebidaADevolver = binding.ivBebida.tag as? Comida
                if (bebidaADevolver != null) {
                    listaComidas.add(bebidaADevolver)
                    adapter.notifyDataSetChanged()
                }
                binding.ivBebida.setImageResource(0)
                binding.ivBebida.tag = null
            }
        }

        binding.rvJuego.adapter = adapter
    }

    private fun generaCliente(): Cliente {
        val nombres = listOf(
            "Carlos", "Ana", "Pedro", "Lucía", "María", "Luis", "Elena", "Javier",
            "Sofía", "Manuel", "Isabel", "David", "Carmen", "Pablo", "Paula", "Sergio",
            "Marta", "Adrián", "Raquel", "Daniel", "Alba", "Jorge", "Cristina", "Álvaro",
            "Laura", "Rubén", "Patricia", "Diego", "Natalia", "Víctor", "Sara", "Guillermo",
            "Andrea", "Ricardo", "Beatriz", "José", "Silvia", "Fernando"
        )

        val fotos = arrayOf(
            R.drawable.pers1,
            R.drawable.pers2,
            R.drawable.pers3,
            R.drawable.pers4
        )

        val nombreAleatorio = nombres.random()
        val alergenoAleatorio = Alergeno.values().random()
        val dietaAleatoria = Dietas.values().random()
        val fotoAleatoria = fotos.random()

        binding.tvAlergiaPers.text = alergenoAleatorio.toString()
        binding.tvNombrePers.text = nombreAleatorio
        binding.tvDietaPers.text = dietaAleatoria.toString()
        binding.ivFotoFicha.setImageResource(fotoAleatoria)

        return Cliente(
            nombre = nombreAleatorio,
            alergeno = alergenoAleatorio,
            dieta = dietaAleatoria,
            foto = fotoAleatoria
        )
    }

    private fun reiniciaTablero(platos: List<ImageView>) {
        binding.ivBebida.setImageResource(0)
        for (plato in platos) {
            plato.setImageResource(0)
        }
    }

    private fun iniciaRecycler() {
        adapter = JuegoAdapter(platosMap) { comida -> onItemSelected(comida) }
        binding.rvJuego.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvJuego.adapter = adapter
    }

    private fun onDeletedItem(position: Int) {
        platosMap.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, platosMap.size)
    }

    private fun onItemSelected(comida: Comida) {
        Toast.makeText(this, comida.nombre, Toast.LENGTH_SHORT).show()
    }

    private fun startCountdownTimer() {
        val countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                binding.tvCrono.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvCrono.text = "00:00"
                binding.enviarbtn.isEnabled = false
                binding.enviarbtn.alpha = 0.5f

                val puntuacion = calculapuntuacion()
                val usuario = Usuario.currentUsuario?.nombre.toString()

                AlertDialog.Builder(this@Jugar)
                    .setTitle("Juego terminado")
                    .setMessage("Usuario: $usuario\nPuntuación: $puntuacion")
                    .setPositiveButton("Volver a jugar") { _, _ ->
                        reiniciaTablero(listOf(binding.imPlato1, binding.imPlato2, binding.imPlato3, binding.imPlato4))
                        binding.btInicio.isEnabled = true
                        binding.btInicio.alpha = 1.0f
                        binding.enviarbtn.isEnabled = false
                        binding.enviarbtn.alpha = 0.5f
                        binding.tvCrono.text = "02:00"
                    }
                    .setNegativeButton("Volver al menú") { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
            }
        }
        countDownTimer.start()
    }
}
