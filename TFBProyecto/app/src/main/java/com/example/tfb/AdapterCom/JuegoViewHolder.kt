package com.example.tfb.AdapterCom

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.databinding.ItemverticalBinding

class JuegoViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemverticalBinding.bind(view)


    fun render(comida: Comida, onClickListener: (Comida) -> Unit, onClickDelete: (Int) -> Unit) {

        binding.ivComida.setOnClickListener{onClickDelete(adapterPosition)}
        binding.ivComida.setImageResource(comida.foto)
        binding.tvComida.text = comida.nombre

    }

    }
