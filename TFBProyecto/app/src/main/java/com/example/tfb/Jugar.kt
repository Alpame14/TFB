package com.example.tfb

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tfb.databinding.ActivityJugarBinding

class Jugar : AppCompatActivity() {
    private lateinit var binding: ActivityJugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.ivJugar.setOnClickListener {


    }
}