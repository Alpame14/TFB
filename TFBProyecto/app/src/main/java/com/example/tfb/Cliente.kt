package com.example.tfb

import com.example.tfb.Enumerados.*

data class Cliente(
    val nombre: String,
    val alergeno: Alergeno,
    val dieta: Dietas,
    val foto: Int // ID del recurso drawable
)