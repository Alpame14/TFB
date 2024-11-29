package com.example.tfb.AdapterCom

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class ComidaViewHolder(view:View): RecyclerView.ViewHolder(view) {

    val foto = view.findViewById<ImageView>(R.id.ivComida)
    val nombrecomida = view.findViewById<TextView>(R.id.tvComida)
    val textoalergia = view.findViewById<TextView>(R.id.tvAlergeno)
    val textocategoria = view.findViewById<TextView>(R.id.tvCategoria)

    fun render(comida: Comida){

        foto.setImageResource(comida.foto)
        nombrecomida.text = comida.nombre
        textoalergia.text = comida.alergeno.toString()
        textocategoria.text = comida.categoria.toString()


    }
}