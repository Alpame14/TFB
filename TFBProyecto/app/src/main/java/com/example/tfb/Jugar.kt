package com.example.tfb

import android.app.AlertDialog
import android.graphics.drawable.Drawable
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
    private var platosMap:MutableList<Comida> = ComidaProvider.listaComida.toMutableList()
    private var copia = platosMap
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
        binding.enviarbtn.isEnabled = false
        binding.enviarbtn.alpha = 0.5f

        reiniciaTablero(platos)

        // Configurar el botón para iniciar el cronómetro
        binding.btInicio.setOnClickListener {
            startCountdownTimer()
            binding.btInicio.isEnabled = false
            binding.btInicio.alpha = 0.5f
            binding.enviarbtn.isEnabled = true
            binding.enviarbtn.alpha = 1.0f

            juego(platos)


        }



        binding.enviarbtn.setOnClickListener {

            calculapuntuacion();
            generaCliente()
            reiniciaTablero(platos)


        }

    }

    private fun calculapuntuacion(): Int {




        return 0
    }

    private fun juego(platos: List<ImageView>) {
        generaCliente()
        iniciaRecycler()

        // Lista para manejar los datos en el RecyclerView
        val listaComidas = platosMap.toMutableList()

        // Configura el adaptador con un listener para actualizar las imágenes
        adapter = JuegoAdapter(listaComidas) { comida ->
            // Guardamos una referencia al objeto seleccionado
            var comidaSeleccionada: Comida? = comida

            if (comida.bebida == true) {
                binding.ivBebida.setImageResource(comida.foto)
                binding.ivBebida.tag = comida // Guardar el objeto en el tag para recuperarlo después
            } else {
                val platos = listOf(
                    binding.imPlato1,
                    binding.imPlato2,
                    binding.imPlato3,
                    binding.imPlato4
                )

                for (plato in platos) {
                    if (plato.drawable == null) {
                        plato.setImageResource(comida.foto)
                        plato.tag = comida // Guardar el objeto en el tag
                        listaComidas.remove(comida) // Lo quitamos de la lista
                        adapter.notifyDataSetChanged() // Notificar al RecyclerView
                        break
                    }
                }
            }

            // Asignar onClickListener a cada plato para devolverlo
            for (plato in platos) {
                plato.setOnClickListener {
                    val comidaADevolver = plato.tag as? Comida
                    if (comidaADevolver != null) {
                        // Devolver a la lista del adaptador
                        listaComidas.add(comidaADevolver)
                        adapter.notifyDataSetChanged() // Actualizar el RecyclerView
                    }
                    plato.setImageResource(0) // Quitar la imagen
                    plato.tag = null // Limpiar el tag
                    Toast.makeText(this, "Plato vaciado", Toast.LENGTH_SHORT).show()
                }
            }

            // Hacer lo mismo con la bebida
            binding.ivBebida.setOnClickListener {
                val bebidaADevolver = binding.ivBebida.tag as? Comida
                if (bebidaADevolver != null) {
                    listaComidas.add(bebidaADevolver)
                    adapter.notifyDataSetChanged() // Actualizar el RecyclerView
                }
                binding.ivBebida.setImageResource(0)
                binding.ivBebida.tag = null
            }
        }

        binding.rvJuego.adapter = adapter
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

        // Array con los IDs de las imágenes en drawable
        val fotos = arrayOf(
            R.drawable.pers1,
            R.drawable.pers2,
            R.drawable.pers3,
            R.drawable.pers4
        )

        val nombreAleatorio = nombres.random() // Elegir un nombre aleatorio del array
        val alergenoAleatorio = Alergeno.values().random() // Elegir un alérgeno aleatorio del enum
        val dietaAleatoria = Dietas.values().random() // Elegir una dieta aleatoria del enum
        val fotoAleatoria = fotos.random() // ID ficticio de drawable

        binding.tvAlergiaPers.text = alergenoAleatorio.toString()
        binding.tvNombrePers.text = nombreAleatorio.toString()
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
        binding.ivBebida.setImageResource(0) // Limpia la imagen
        for (plato in platos) {
            plato.setImageResource(0)
        }
    }


    private fun iniciaRecycler() {
        adapter = JuegoAdapter(
            platosMap,
            onClickListener = { comida -> onItemSelected(comida)},

        )
        binding.rvJuego.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvJuego.adapter = adapter
    }


    private fun onDeletedItem(position: Int) {
        platosMap.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, platosMap.size)
    }


    private fun onItemSelected(comida: Comida) {
        Toast.makeText(this,comida.nombre,Toast.LENGTH_SHORT).show()

    }

    // Métodos para gestionar la puntuación máxima (implementación ficticia)
    private fun obtenerPuntuacionMaxima(): Int {
        // Aquí puedes usar SharedPreferences para obtener la puntuación máxima guardada
        val sharedPreferences = getSharedPreferences("JuegoPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("puntuacionMaxima", 0)
    }

    private fun guardarPuntuacionMaxima(puntuacion: Int) {
        // Guarda la nueva puntuación máxima en SharedPreferences
        val sharedPreferences = getSharedPreferences("JuegoPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("puntuacionMaxima", puntuacion).apply()
    }

    private fun startCountdownTimer() {
        val countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                binding.tvCrono.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Mostrar 00:00 cuando el tiempo termine
                binding.tvCrono.text = "00:00"

                // Desactivar el botón de enviar
                binding.enviarbtn.isEnabled = false
                binding.enviarbtn.alpha = 0.5f

                // Detener el juego y mostrar el AlertDialog
                val puntuacion = calculapuntuacion() // Obtén la puntuación del método
                val usuario = Usuario.currentUsuario?.nombre.toString() // Obtén el nombre del cliente actual
                val puntuacionMaxima = calculapuntuacion() // Método ficticio para obtener la puntuación máxima guardada
                var mensajeExtra = ""

                // Verificar si la puntuación supera la máxima registrada
                if (puntuacion > puntuacionMaxima) {
                    mensajeExtra = "\n¡Nueva puntuación máxima!"
                    guardarPuntuacionMaxima(puntuacion) // Método ficticio para guardar la nueva puntuación máxima
                }

                // Crear y mostrar el AlertDialog
                AlertDialog.Builder(this@Jugar)
                    .setTitle("Juego terminado")
                    .setMessage(
                        "Usuario: $usuario\n" +
                                "Puntuación: $puntuacion$mensajeExtra"
                    )
                    .setPositiveButton("Volver a jugar") { _, _ ->
                        // Reiniciar el juego
                        reiniciaTablero(listOf(binding.imPlato1, binding.imPlato2, binding.imPlato3, binding.imPlato4))
                        binding.btInicio.isEnabled = true
                        binding.btInicio.alpha = 1.0f
                        binding.enviarbtn.isEnabled = false
                        binding.enviarbtn.alpha = 0.5f
                        binding.tvCrono.text = "02:00" // Reinicia el cronómetro
                    }
                    .setNegativeButton("Volver al menú") { _, _ ->
                        // Volver a la pantalla principal
                        finish() // Cierra la actividad actual
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        countDownTimer.start()
    }
}
