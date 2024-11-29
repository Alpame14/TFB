package com.example.tfb.AdapterCom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class ComidaAdapter(private val listaComidas:List<Comida>) : RecyclerView.Adapter<ComidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ComidaViewHolder(layoutInflater.inflate(R.layout.item_comida,parent,false))
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {

        val item = listaComidas[position]
        holder.render(item)


    }

    override fun getItemCount(): Int = listaComidas.size

}