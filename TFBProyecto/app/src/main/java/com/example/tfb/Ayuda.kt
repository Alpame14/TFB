package com.example.tfb

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfb.AdapterCom.ComidaAdapter
import com.example.tfb.AdapterCom.ComidaProvider
import com.example.tfb.databinding.ActivityAyudaBinding
import com.example.tfb.databinding.ActivityMainBinding

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
        iniciaRecycler()



    }

    private fun iniciaRecycler() {
        binding.rvAyuda.layoutManager = LinearLayoutManager(this)
        binding.rvAyuda.adapter = ComidaAdapter(ComidaProvider.listaComida)
    }
}