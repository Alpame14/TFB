package com.example.tfb.AdapterCom

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.databinding.ItemverticalBinding

class JuegoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemverticalBinding.bind(view)

    fun render(comida: Comida, position: Int, onClickListener: (Int) -> Unit) {
        binding.ivComida.setImageResource(comida.foto)
        binding.tvComida.text = comida.nombre

        // Cuando se haga clic en la imagen, se eliminará el objeto de la RecyclerView
        binding.ivComida.setOnClickListener {
            onClickListener(position)
        }
    }
}

