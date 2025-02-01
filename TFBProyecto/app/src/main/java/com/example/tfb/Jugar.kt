package com.example.tfb

import android.app.AlertDialog
import android.graphics.drawable.Drawable
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

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding
    private lateinit var adapter: JuegoAdapter
    private var platosMap: MutableList<Comida> = ComidaProvider.listaComida.toMutableList()
    private var puntuacion: Double = 0.0
    private var grasas: Double = 0.0
    private var cal: Double = 0.0
    private var multi: Double = 0.0
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
            calculaPuntuacion(platos)
            generaCliente()
            reiniciaTablero(platos)

            grasas = 0.0
            cal = 0.0
            multi = 0.0

            // 🔹 Generar nueva lista de 10 comidas aleatorias
            platosMap.clear()  // ✅ Limpiar la lista anterior
            platosMap.addAll(ComidaProvider.listaComida.shuffled().take(10))

            // 🔹 Notificar cambios en el adaptador sin cambiar su referencia
            adapter.notifyDataSetChanged()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        // ✅ Detener la música cuando se cierre la Activity
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun calculaPuntuacion(platos: List<ImageView>) {
        var pivot: Comida? = null
        var media:Double
        for (plato in platos) {

            pivot = plato.tag as? Comida  // Intenta castear a Comida de forma segura
            comprobaciones(pivot)

        }
        pivot = binding.ivBebida.tag as? Comida
        comprobaciones(pivot)

        puntuacion += 50
        if (300 <= cal && cal <= 450) {
            puntuacion += 40
        } else {
            puntuacion -= 40
        }
        media = (grasas / cal) * 100
        if (10 <= media && media <= 35) {
            puntuacion += 30
        } else {
            puntuacion -= 20
        }
        puntuacion *= multi
        binding.tvPuntuacion.text = puntuacion.toString()

    }

    private fun comprobaciones(pivot: Comida?) {


        if (pivot != null) {

            //controlamos alergenos
            if (pivot?.alergeno != null && pivot.alergeno.equals(binding.tvAlergiaPers.text.toString())) {
                puntuacion = 0.0
                //controlamos dieta
            } else if (binding.tvDietaPers.text.toString()
                    .equals(Dietas.Vegano) && (pivot.categoria == Categoria.Origen_Animal || pivot.categoria == Categoria.Lacteo) || (binding.tvDietaPers.text.toString()
                    .equals(Dietas.Vegetariano) && pivot.categoria == Categoria.Origen_Animal)
            ) {
                puntuacion = puntuacion / 2
            } else {
                cal += pivot.calorias
                grasas += pivot.grasas * 9
                multi = 1 + (pivot.minerales * 0.2) + (pivot.proteinas * 0.5) + (pivot.vitaminas * 0.1)


            }


        }

    }


    private fun juego(platos: List<ImageView>) {
        generaCliente()
        iniciaRecycler()

        platosMap = ComidaProvider.listaComida.shuffled().take(10).toMutableList()
        adapter = JuegoAdapter(platosMap) { comida ->
            if (comida.bebida == true) {
                binding.ivBebida.setImageResource(comida.foto)
                binding.ivBebida.tag = comida
            } else {
                for (plato in platos) {
                    if (plato.drawable == null) {
                        plato.setImageResource(comida.foto)
                        plato.tag = comida
                        platosMap.remove(comida) // ✅ Eliminar de la lista
                        adapter.notifyDataSetChanged()
                        break
                    }
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
        for (plato in platos) {
            plato.setImageResource(0)
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

                val usuario = Usuario.currentUsuario?.nombre.toString()

                AlertDialog.Builder(this@Jugar)
                    .setTitle("Juego terminado")
                    .setMessage("Usuario: $usuario\nPuntuación: $puntuacion")
                    .setPositiveButton("Volver a jugar") { _, _ ->
                        reiniciaTablero(
                            listOf(
                                binding.imPlato1,
                                binding.imPlato2,
                                binding.imPlato3,
                                binding.imPlato4
                            )
                        )
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
