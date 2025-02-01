package com.example.tfb

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tfb.AdapterCom.ComidaProvider
import com.example.tfb.AdapterCom.JuegoAdapter
import com.example.tfb.databinding.ActivityJugarBinding
import com.example.tfb.Enumerados.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.roundToInt

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding
    private lateinit var adapter: JuegoAdapter
    private var platosMap: MutableList<Comida> = ComidaProvider.listaComida.toMutableList()
    private var puntuacion: Double = 0.0
    private var grasas: Double = 0.0
    private var cal: Double = 0.0
    private var multi: Double = 0.0
    private var mediaPlayer: MediaPlayer? = null
    private var countDownTimer: CountDownTimer? = null // ✅ Control del temporizador

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

        binding.btInicio.setOnClickListener {
            startCountdownTimer()
            binding.btInicio.isEnabled = false
            binding.btInicio.alpha = 0.5f
            binding.enviarbtn.isEnabled = true
            binding.enviarbtn.alpha = 1.0f

            juego(platos)

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cancion_juego)
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            }
        }

        binding.enviarbtn.setOnClickListener {
            if (todosLosPlatosLlenos(platos)) {
                calculaPuntuacion(platos)
                generaCliente()
                reiniciaTablero(platos)

                grasas = 0.0
                cal = 0.0
                multi = 0.0

                platosMap.clear()
                platosMap.addAll(ComidaProvider.listaComida.shuffled().take(10))
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Debes llenar todos los platos y la bebida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun calculaPuntuacion(platos: List<ImageView>) {
        var pivot: Comida?

        for (plato in platos) {
            pivot = plato.tag as? Comida
            if (!comprobaciones(pivot)) return
        }

        pivot = binding.ivBebida.tag as? Comida
        if (!comprobaciones(pivot)) return

        puntuacion += 50
        if (300 <= cal && cal <= 450) {
            puntuacion += 40
        } else {
            puntuacion -= 40
        }
        val media = (grasas / cal) * 100
        if (5 <= media && media <= 40) {
            puntuacion += 30
        } else {
            puntuacion -= 20
        }

        puntuacion *= multi
        puntuacion = puntuacion.roundToInt().toDouble()
        binding.tvPuntuacion.text = puntuacion.toInt().toString()
    }

    private fun comprobaciones(pivot: Comida?): Boolean {
        if (pivot == null) {
            puntuacion -= 20
            return true
        }

        if (pivot.alergeno?.toString() == binding.tvAlergiaPers.text.toString()) {
            puntuacion = 0.0
            mostrarGameOver()
            return false
        }

        if ((binding.tvDietaPers.text.toString() == Dietas.Vegano.toString() &&
                    (pivot.categoria == Categoria.Animal || pivot.categoria == Categoria.Lacteo)) ||
            (binding.tvDietaPers.text.toString() == Dietas.Vegetariano.toString() &&
                    pivot.categoria == Categoria.Animal)
        ) {
            puntuacion = 0.0
            mostrarGameOver()
            return false
        }

        cal += pivot.calorias
        grasas += pivot.grasas * 9
        multi = 1 + (pivot.minerales * 0.2) + (pivot.proteinas * 0.5) + (pivot.vitaminas * 0.1)
        return true
    }

    private fun mostrarGameOver() {
        countDownTimer?.cancel() // ✅ Detener el temporizador si el jugador pierde
        AlertDialog.Builder(this@Jugar)
            .setTitle("¡Perdiste!")
            .setMessage("Has fallado en alérgenos o dieta. Puntuación: 0")
            .setPositiveButton("Volver a jugar") { _, _ ->
                reiniciarJuego()
            }
            .setNegativeButton("Volver al menú") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }


    private fun juego(platos: List<ImageView>) {
        generaCliente()
        iniciaRecycler()

        platosMap = ComidaProvider.listaComida.shuffled().take(12).toMutableList()
        adapter = JuegoAdapter(platosMap) { comida ->
            if (comida.bebida == true) {
                // ✅ Verifica si hay espacio para la bebida antes de agregarla
                if (binding.ivBebida.drawable == null) {
                    binding.ivBebida.setImageResource(comida.foto)
                    binding.ivBebida.tag = comida
                    platosMap.remove(comida) // ✅ Eliminar solo si se pudo agregar
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No hay espacio para más bebidas", Toast.LENGTH_SHORT).show()
                }
            } else {
                var agregado = false
                for (plato in platos) {
                    if (plato.drawable == null) { // ✅ Verifica si hay un espacio vacío
                        plato.setImageResource(comida.foto)
                        plato.tag = comida
                        platosMap.remove(comida) // ✅ Eliminar solo si se pudo agregar
                        adapter.notifyDataSetChanged()
                        agregado = true
                        break
                    }
                }
                if (!agregado) {
                    Toast.makeText(this, "No hay espacio en los platos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvJuego.adapter = adapter

        // 🔹 Permitir devolver los platos a la RecyclerView
        for (plato in platos) {
            plato.setOnClickListener {
                val comidaADevolver = plato.tag as? Comida
                if (comidaADevolver != null) {
                    platosMap.add(comidaADevolver) // ✅ Devuelve la comida
                    adapter.notifyDataSetChanged()
                    plato.setImageResource(0) // ✅ Limpia el plato
                    plato.tag = null
                    Toast.makeText(this, "Plato devuelto", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 🔹 Permitir devolver bebidas a la RecyclerView
        binding.ivBebida.setOnClickListener {
            val bebidaADevolver = binding.ivBebida.tag as? Comida
            if (bebidaADevolver != null) {
                platosMap.add(bebidaADevolver)
                adapter.notifyDataSetChanged()
                binding.ivBebida.setImageResource(0)
                binding.ivBebida.tag = null
            }
        }
    }


    private fun generaCliente() {
        val db = FirebaseFirestore.getInstance()

        db.collection("config").document("valores_generales")
            .get()
            .addOnSuccessListener { document ->

                val nombres = document.get("nombres") as? List<String> ?: emptyList()
                val fotos = document.get("fotos") as? List<String> ?: emptyList()

                if (nombres.isNotEmpty() && fotos.isNotEmpty()) {
                    val nombreAleatorio = nombres.random()
                    val fotoAleatoria = fotos.random()
                    val alergenoAleatorio = Alergeno.values().random()
                    val dietaAleatoria = Dietas.values().random()

                    // Asignar a la UI
                    binding.tvNombrePers.text = nombreAleatorio
                    binding.tvAlergiaPers.text = alergenoAleatorio.toString()
                    binding.tvDietaPers.text = dietaAleatoria.toString()
                    // Obtén la referencia al archivo en Firebase Storage
                    val storageRef =
                        FirebaseStorage.getInstance().getReferenceFromUrl(fotoAleatoria)

                    // Obtiene la URL de descarga
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val urlDescarga = uri.toString()


                        // Cargar la imagen con Glide
                        Glide.with(binding.ivFotoFicha.context)
                            .load(urlDescarga)
                            .into(binding.ivFotoFicha)
                    }.addOnFailureListener {
                    }
                }

            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al obtener datos", e)
            }
    }


    private fun reiniciaTablero(platos: List<ImageView>) {
        binding.ivBebida.setImageResource(0)
        binding.ivBebida.tag = null
        for (plato in platos) {
            plato.setImageResource(0)
            plato.tag = null
        }
    }

    private fun iniciaRecycler() {
        adapter = JuegoAdapter(platosMap) { comida -> onItemSelected(comida) }
        binding.rvJuego.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
        countDownTimer?.cancel() // ✅ Asegurar que el temporizador anterior se detenga antes de iniciar uno nuevo
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                binding.tvCrono.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvCrono.text = "00:00"
                binding.enviarbtn.isEnabled = false
                binding.enviarbtn.alpha = 0.5f

                val usuario = Usuario.currentUsuario?.nombre.toString()
                AlertDialog.Builder(this@Jugar)
                    .setTitle("Juego terminado")
                    .setMessage("Usuario: $usuario\nPuntuación: $puntuacion")
                    .setPositiveButton("Volver a jugar") { _, _ -> reiniciarJuego() }
                    .setNegativeButton("Volver al menú") { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
            }
        }.start()
    }
    private fun reiniciarJuego() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun todosLosPlatosLlenos(platos: List<ImageView>): Boolean {
        return platos.all { it.drawable != null } && binding.ivBebida.drawable != null
    }

}
