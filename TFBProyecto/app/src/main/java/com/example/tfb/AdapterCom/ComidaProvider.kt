package com.example.tfb.AdapterCom

import com.example.tfb.Alergeno
import com.example.tfb.Categoria
import com.example.tfb.Comida
import com.example.tfb.R

class ComidaProvider {
    companion object{
        val listaComida = listOf<Comida>(
            Comida(
            nombre = "Manzana",
            alergeno = Alergeno.Ninguno,
            categoria = Categoria.Fruta,
            bebida = false,
            proteinas = 0.3,
            grasas = 0.2,
            minerales = 0.1,
            vitaminas = 8.4,
            fibra = 2.4,
            calorias = 52,
            foto = R.drawable.al_manzana // ID del drawable de la imagen
        ),
            Comida(
                nombre = "Banana",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Fruta,
                bebida = false,
                proteinas = 1.1,
                grasas = 0.3,
                minerales = 0.8,
                vitaminas = 9.0,
                fibra = 2.6,
                calorias = 89,
                foto = R.drawable.al_platano // ID del drawable
            ),

            Comida(
                nombre = "Jugo de Naranja",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.7,
                grasas = 0.2,
                minerales = 0.1,
                vitaminas = 50.0,
                fibra = 0.2,
                calorias = 45,
                foto = R.drawable.be_zumonaranja
            )
        )

    }
}