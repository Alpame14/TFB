package com.example.tfb.AdapterCom


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class JuegoAdapter(
    private val listaComidas: List<Comida>,
    private val onClickListener: (Comida) -> Unit, // Callback para manejar clics
    private val onClickDelete:(Int) -> Unit
) : RecyclerView.Adapter<JuegoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegoViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return JuegoViewHolder(layoutInflater.inflate(R.layout.itemvertical,parent,false))
    }


    override fun onBindViewHolder(holder: JuegoViewHolder, position: Int) {

        val item = listaComidas[position]
        holder.render(item,onClickListener,onClickDelete)

    }

    override fun getItemCount(): Int = listaComidas.size



}