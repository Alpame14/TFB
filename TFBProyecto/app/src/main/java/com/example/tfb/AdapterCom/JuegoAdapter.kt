package com.example.tfb.AdapterCom


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfb.Comida
import com.example.tfb.R

class JuegoAdapter(
    private val listaComidas: MutableList<Comida>,
    private val onClickListener: (Comida) -> Unit
) : RecyclerView.Adapter<JuegoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return JuegoViewHolder(layoutInflater.inflate(R.layout.itemvertical, parent, false))
    }

    override fun onBindViewHolder(holder: JuegoViewHolder, position: Int) {
        val item = listaComidas[position]
        holder.render(item, position) {
            // Enviar el objeto Comida al método de la actividad/fragmento en vez de eliminarlo aquí
            onClickListener(item)
            removeItem(item) // Eliminamos el item del adaptador cuando se hace clic en el plato
        }
    }

    // Elimina el item de la lista y actualiza el RecyclerView
    fun removeItem(comida: Comida) {
        val position = listaComidas.indexOf(comida)
        if (position != -1) {
            listaComidas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listaComidas.size)
        }
    }

    override fun getItemCount(): Int = listaComidas.size
}
