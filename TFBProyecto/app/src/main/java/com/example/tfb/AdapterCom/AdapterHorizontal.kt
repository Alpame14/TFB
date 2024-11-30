package com.example.tfb.AdapterCom


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class AdapterHorizontal(private val listaComidas:List<Comida>) : RecyclerView.Adapter<ViewHolderHorizontal>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHorizontal {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderHorizontal(layoutInflater.inflate(R.layout.itemvertical,parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolderHorizontal, position: Int) {

        val item = listaComidas[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = listaComidas.size



}