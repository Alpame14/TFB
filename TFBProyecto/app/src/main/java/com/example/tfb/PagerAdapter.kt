package com.example.tfb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2 // Número de pestañas

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AyudaAlimentosFragment() // Primer fragment
            1 -> AcercaDeFragment() // Segundo fragment
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
