package com.example.tfb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfb.AdapterCom.ComidaAdapter
import com.example.tfb.AdapterCom.ComidaProvider
import com.example.tfb.AdapterCom.ComidaProvider.Companion.listaComida
import com.example.tfb.Enumerados.*
import com.example.tfb.databinding.FragmentAyudaAlimentosBinding

class AyudaAlimentosFragment : Fragment() {

    private var _binding: FragmentAyudaAlimentosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyudaAlimentosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaFiltrada: MutableList<Comida> = mutableListOf()
        iniciaRecycler(listaComida)

        // Configurar el Spinner con fondo blanco y texto negro
        val opciones = Categoria.values().map { it.name }
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            opciones
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(resources.getColor(android.R.color.black, null)) // Texto negro
                view.textSize = 16f
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(resources.getColor(android.R.color.black, null)) // Texto negro
                view.setBackgroundColor(resources.getColor(android.R.color.white, null)) // Fondo blanco
                view.textSize = 16f
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                val area = binding.spinner.selectedItem.toString()
                if (area == "Ninguno") {
                    iniciaRecycler(listaComida)
                } else {
                    filtrarLista(area, listaFiltrada)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                iniciaRecycler(listaComida)
            }
        }
    }

    private fun filtrarLista(area: String, listaFiltrada: MutableList<Comida>) {
        listaFiltrada.clear()

        for (comida in ComidaProvider.listaComida) {
            if (comida.categoria.toString() == area) {
                listaFiltrada.add(comida)
            }
        }

        val adapter = ComidaAdapter(listaFiltrada)
        binding.rvAyuda.adapter = adapter
    }

    private fun iniciaRecycler(lista: List<Comida>) {
        binding.rvAyuda.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAyuda.adapter = ComidaAdapter(lista)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
