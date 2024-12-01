package com.example.tfb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfb.AdapterCom.ComidaAdapter
import com.example.tfb.AdapterCom.ComidaProvider
import com.example.tfb.AdapterCom.ComidaProvider.Companion.listaComida
import com.example.tfb.Enumerados.*
import com.example.tfb.databinding.ActivityAyudaBinding


class Ayuda : AppCompatActivity() {
    lateinit var binding: ActivityAyudaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAyudaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listaFiltrada: MutableList<Comida> = mutableListOf()
        iniciaRecycler(listaComida)

        // Configura el Spinner
        val opciones = Categoria.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                val area = binding.spinner.selectedItem.toString()
                if (area == "Ninguno") {
                    iniciaRecycler(listaComida)
                } else {
                    filtrarLista(area,listaFiltrada)
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

        var adapter = ComidaAdapter(listaFiltrada)
        binding.rvAyuda.setAdapter(adapter)
    }

    private fun iniciaRecycler(lista: List<Comida>) {
        binding.rvAyuda.layoutManager = LinearLayoutManager(this)
        binding.rvAyuda.adapter = ComidaAdapter(lista)
    }
}
