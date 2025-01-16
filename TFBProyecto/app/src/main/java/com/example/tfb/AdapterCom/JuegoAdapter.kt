package com.example.tfb.AdapterCom


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class JuegoAdapter(
    private val listaComidas: List<Comida>,
    private val onItemClick: (Comida) -> Unit // Callback para manejar clics
) : RecyclerView.Adapter<JuegoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegoViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return JuegoViewHolder(layoutInflater.inflate(R.layout.itemvertical,parent,false))
    }


    override fun onBindViewHolder(holder: JuegoViewHolder, position: Int) {

        val item = listaComidas[position]
        holder.render(item)

        // Manejar el clic usando el callback
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun getItemCount(): Int = listaComidas.size



}