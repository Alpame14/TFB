package com.example.tfb

import java.io.Serializable
import com.example.tfb.Enumerados.*

data class Comida(
    val nombre: String,
    val alergeno: Alergeno,
    val categoria: Categoria,
    val bebida: Boolean,
    val proteinas: Double,       // Cantidad de proteínas en gramos
    val grasas: Double,          // Cantidad de grasas en gramos
    val minerales: Double,       // Cantidad de minerales en gramos
    val vitaminas: Double,       // Cantidad de vitaminas en miligramos
    val fibra: Double,           // Cantidad de fibra en gramos
    val calorias: Int,          // Calorías totales en kcal
    val foto: Int // ID del recurso drawable
) : Serializable


