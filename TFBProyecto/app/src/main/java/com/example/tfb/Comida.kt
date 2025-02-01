package com.example.tfb

import java.io.Serializable
import com.example.tfb.Enumerados.*

data class Comida(
    val nombre: String,
    val alergeno: Alergeno,
    val categoria: Categoria,
    val bebida: Boolean,
    val proteinas: Double,       // Cantidad de proteínas en gramos 8,2
    val grasas: Double,          // Cantidad de grasas en gramos 20%-30% 1 g grasa = 9 cal
    val minerales: Double,       // Cantidad de minerales en gramos
    val vitaminas: Double,       // Cantidad de vitaminas en miligramos
    val calorias: Int,          // Calorías totales en kcal 350-400
    val foto: Int // ID del recurso drawable
) : Serializable


