package com.example.tfb.AdapterCom

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R
import com.example.tfb.databinding.ItemComidaBinding

class ComidaViewHolder(view:View): RecyclerView.ViewHolder(view) {

    val binding = ItemComidaBinding.bind(view)


    fun render(comida: Comida){

        binding.ivComida.setImageResource(comida.foto)
        binding.tvComida.text = comida.nombre
        binding.tvAlergeno.text = comida.alergeno.toString()
        binding.tvCategoria.text = comida.categoria.toString()

        itemView.setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(itemView.context)
                .setTitle(comida.nombre)
                .setMessage("""
            Alergeno: ${comida.alergeno}
            Categoría: ${comida.categoria}
            Bebida: ${if (comida.bebida) "Sí" else "No"}
            Proteínas: ${comida.proteinas} g
            Grasas: ${comida.grasas} g
            Minerales: ${comida.minerales} g
            Vitaminas: ${comida.vitaminas} mg
            Calorías: ${comida.calorias} kcal
        """.trimIndent())
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()

            alertDialog.show()

            true // IMPORTANTE: Devuelve true para indicar que el evento fue manejado
        }


    }
}