package com.example.tfb

import java.io.Serializable

data class Comidas(val nombre: String,val alergeno: Alergeno, val categoria: Categoria): Serializable{
    //calorías, proteínas, hidratos de carbono, lípidos, minerales, vitaminas, agua y fibra

}
