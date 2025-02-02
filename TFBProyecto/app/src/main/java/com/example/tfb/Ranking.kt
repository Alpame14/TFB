package com.example.tfb

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityRankingBinding
import com.google.firebase.firestore.FirebaseFirestore

class Ranking : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rankingContainer = findViewById<LinearLayout>(R.id.llRankingContainer)

        obtenerUsuarios { ranking ->
            var contador = 0
            for (entry in ranking) {
                contador++
                val row = layoutInflater.inflate(R.layout.ranking_row, rankingContainer, false)

                val ivRank = row.findViewById<ImageView>(R.id.ivRank)
                val rankImageId = resources.getIdentifier(
                    "rank_%02d".format(contador),
                    "drawable",
                    packageName
                )
                ivRank.setImageResource(rankImageId)

                val tvUsername = row.findViewById<TextView>(R.id.tvUsername)
                tvUsername.text = entry.nombre

                val tvScore = row.findViewById<TextView>(R.id.tvScore)
                tvScore.text = entry.maxscore.toString()

                rankingContainer.addView(row)
            }
        }

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val maxscore = prefs.getInt("maxscore", 0)
        binding.tvr.text = maxscore.toString()
    }

    private fun obtenerUsuarios(callback: (MutableList<Usuario>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val coleccion = "users"

        db.collection(coleccion).get()
            .addOnSuccessListener { result ->
                val listaUsuarios = mutableListOf<Usuario>()
                for (documento in result) {
                    val nombre = documento.getString("username") ?: "Sin nombre"
                    val email = documento.getString("email") ?: ""
                    val provider = documento.getString("provider") ?: "NINGUNO"
                    val maxscore = documento.getLong("maxscore")?.toInt() ?: 0
                    val foto = documento.getString("foto")

                    val usuario = Usuario(nombre, email, Enumerados.ProviderType.valueOf(provider), maxscore, foto)
                    println("Usuario obtenido: $nombre, Score: $maxscore") // Verifica que ahora sí aparece el nombre
                    listaUsuarios.add(usuario)
                }
                listaUsuarios.sortByDescending { it.maxscore }
                callback(listaUsuarios.take(10).toMutableList())
            }

    }
}
