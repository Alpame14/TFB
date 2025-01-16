package com.example.tfb.AdapterCom

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.databinding.ItemverticalBinding

class JuegoViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemverticalBinding.bind(view)


    fun render(comida: Comida) {

        binding.ivComida.setImageResource(comida.foto)
        binding.tvComida.text = comida.nombre

    }

    }
