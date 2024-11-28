package com.example.tfb

import android.graphics.Color
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Ranking : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        // Lista de jugadores ficticios
        val players = mutableListOf<Player>(
            Player("Alice", Random.nextInt(0, 10000)),
            Player("Bob", Random.nextInt(0, 10000)),
            Player("Charlie", Random.nextInt(0, 10000)),
            Player("Diana", Random.nextInt(0, 10000)),
            Player("Edward", Random.nextInt(0, 10000)),
            Player("Fiona", Random.nextInt(0, 10000)),
            Player("George", Random.nextInt(0, 10000)),
            Player("Hannah", Random.nextInt(0, 10000)),
            Player("Irene", Random.nextInt(0, 10000)),
            Player("Jack", Random.nextInt(0, 10000))
        )

        // Ordenar jugadores por puntuación (de mayor a menor)
        players.sortByDescending { it.score }

        // Agregar filas con los datos del ranking
        for ((index, player) in players.withIndex()) {
            val tableRow = TableRow(this)

            // Posición
            val positionView = TextView(this).apply {
                text = (index + 1).toString()
                setPadding(8, 8, 8, 8)
                gravity = android.view.Gravity.CENTER
                setTextColor(Color.WHITE)
            }

            // Nombre
            val nameView = TextView(this).apply {
                text = player.name
                setPadding(8, 8, 8, 8)
                gravity = android.view.Gravity.CENTER
                setTextColor(Color.WHITE)
            }

            // Puntuación
            val scoreView = TextView(this).apply {
                text = player.score.toString()
                setPadding(8, 8, 8, 8)
                gravity = android.view.Gravity.CENTER
                setTextColor(Color.WHITE)
            }

            // Añadir vistas a la fila
            tableRow.addView(positionView)
            tableRow.addView(nameView)
            tableRow.addView(scoreView)

        }
    }

    // Clase para representar a un jugador
    data class Player(val name: String, val score: Int)
}
