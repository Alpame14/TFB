package com.example.tfb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityMainAyudaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainAyuda : AppCompatActivity() {

    private lateinit var binding: ActivityMainAyudaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAyudaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar ViewPager2 con PagerAdapter
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Vincular TabLayout con ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Alimentos" // Nombre de la primera pestaña
                1 -> "Acerca De" // Nombre de la segunda pestaña
                else -> "Tab $position"
            }
        }.attach()
    }
}
