package com.example.tfb.AdapterCom

import com.example.tfb.Enumerados.*
import com.example.tfb.Comida
import com.example.tfb.R

class ComidaProvider {
    companion object{
        val listaComida = listOf<Comida>(

            Comida(
                nombre = "Aceite",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Ninguno,
                bebida = false,
                proteinas = 0.0,
                grasas = 100.0,
                minerales = 0.0,
                vitaminas = 0.0,
                calorias = 884,
                foto = R.drawable.al_aceite
            ),

            Comida(
                nombre = "Bacon",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 37.0,
                grasas = 42.0,
                minerales = 2.5,
                vitaminas = 10.0,
                calorias = 541,
                foto = R.drawable.al_bacon
            ),

            Comida(
                nombre = "Cereales",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 7.5,
                grasas = 1.9,
                minerales = 3.0,
                vitaminas = 6.2,
                calorias = 379,
                foto = R.drawable.al_cereales
            ),

            Comida(
                nombre = "Croissant",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 8.2,
                grasas = 21.0,
                minerales = 1.5,
                vitaminas = 2.8,
                calorias = 406,
                foto = R.drawable.al_croissant
            ),

            Comida(
                nombre = "Donut",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 4.9,
                grasas = 22.0,
                minerales = 2.0,
                vitaminas = 1.2,
                calorias = 452,
                foto = R.drawable.al_donut
            ),

            Comida(
                nombre = "Galletas",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 5.6,
                grasas = 21.5,
                minerales = 2.3,
                vitaminas = 4.0,
                calorias = 480,
                foto = R.drawable.al_galletas
            ),

            Comida(
                nombre = "Gamba",
                alergeno = Alergeno.Crustaceos,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 20.3,
                grasas = 1.5,
                minerales = 3.0,
                vitaminas = 5.0,
                calorias = 99,
                foto = R.drawable.al_gamba,
            ),

            Comida(
                nombre = "Huevo",
                alergeno = Alergeno.Huevo,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 13.0,
                grasas = 10.0,
                minerales = 2.1,
                vitaminas = 8.5,
                calorias = 155,
                foto = R.drawable.al_huevo,
            ),

            Comida(
                nombre = "Lechuga",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Verdura,
                bebida = false,
                proteinas = 1.3,
                grasas = 0.2,
                minerales = 2.5,
                vitaminas = 12.1,
                calorias = 14,
                foto = R.drawable.al_lechuga,
            ),

            Comida(
                nombre = "Magdalena",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 5.7,
                grasas = 19.5,
                minerales = 2.2,
                vitaminas = 3.4,
                calorias = 430,
                foto = R.drawable.al_magdalena,
            ),

            Comida(
                nombre = "Manzana",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Fruta,
                bebida = false,
                proteinas = 0.3,
                grasas = 0.2,
                minerales = 0.1,
                vitaminas = 8.4,
                calorias = 52,
                foto = R.drawable.al_manzana // ID del drawable de la imagen
            ),

            Comida(
                nombre = "Mermelada",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Ninguno,
                bebida = false,
                proteinas = 0.4,
                grasas = 0.1,
                minerales = 0.5,
                vitaminas = 2.0,
                calorias = 250,
                foto = R.drawable.al_mermelada,
            ),

            Comida(
                nombre = "Nueces",
                alergeno = Alergeno.Frutos_Secos,
                categoria = Categoria.Ninguno,
                bebida = false,
                proteinas = 15.0,
                grasas = 65.0,
                minerales = 2.5,
                vitaminas = 3.1,
                calorias = 654,
                foto = R.drawable.al_nueces,
            ),

            Comida(
                nombre = "Pan",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Cereal,
                bebida = false,
                proteinas = 8.9,
                grasas = 1.2,
                minerales = 3.5,
                vitaminas = 4.3,
                calorias = 265,
                foto = R.drawable.al_pan,
            ),

            Comida(
                nombre = "Pescado",
                alergeno = Alergeno.Pescado,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 20.0,
                grasas = 5.0,
                minerales = 2.8,
                vitaminas = 9.0,
                calorias = 206,
                foto = R.drawable.al_pescado,
            ),

            Comida(
                nombre = "Plátano",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Fruta,
                bebida = false,
                proteinas = 1.1,
                grasas = 0.3,
                minerales = 0.8,
                vitaminas = 9.0,
                calorias = 89,
                foto = R.drawable.al_platano // ID del drawable
            ),

            Comida(
                nombre = "Queso",
                alergeno = Alergeno.Lacteos,
                categoria = Categoria.Lacteo,
                bebida = false,
                proteinas = 25.0,
                grasas = 30.0,
                minerales = 3.8,
                vitaminas = 10.5,
                calorias = 402,
                foto = R.drawable.al_queso,
            ),

            Comida(
                nombre = "Salchicha",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 12.0,
                grasas = 28.0,
                minerales = 2.0,
                vitaminas = 3.5,
                calorias = 320,
                foto = R.drawable.al_salchicha,
            ),

            Comida(
                nombre = "Yogurt",
                alergeno = Alergeno.Lacteos,
                categoria = Categoria.Lacteo,
                bebida = false,
                proteinas = 4.3,
                grasas = 3.5,
                minerales = 1.2,
                vitaminas = 5.4,
                calorias = 59,
                foto = R.drawable.al_yogurt,
            ),

            Comida(
                nombre = "Jamón York",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Animal,
                bebida = false,
                proteinas = 18.5,
                grasas = 7.3,
                minerales = 2.0,
                vitaminas = 3.5,
                calorias = 145,
                foto = R.drawable.al_york,
            ),

            Comida(
                nombre = "Agua",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.0,
                grasas = 0.0,
                minerales = 0.0,
                vitaminas = 0.0,
                calorias = 0,
                foto = R.drawable.be_agua,
            ),

            Comida(
                nombre = "Bebida Energética",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.0,
                grasas = 0.0,
                minerales = 0.2,
                vitaminas = 3.5,
                calorias = 45,
                foto = R.drawable.be_energetica,
            ),

            Comida(
                nombre = "Café",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.3,
                grasas = 0.2,
                minerales = 0.5,
                vitaminas = 1.0,
                calorias = 2,
                foto = R.drawable.be_cafe,
            ),

            Comida(
                nombre = "Cerveza",
                alergeno = Alergeno.Gluten,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.5,
                grasas = 0.0,
                minerales = 0.3,
                vitaminas = 0.5,
                calorias = 43,
                foto = R.drawable.be_cerveza,
            ),


            Comida(
                nombre = "Leche",
                alergeno = Alergeno.Lacteos,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 3.4,
                grasas = 3.2,
                minerales = 1.0,
                vitaminas = 4.5,
                calorias = 61,
                foto = R.drawable.be_leche,
            ),

            Comida(
                nombre = "Leche de Almendra",
                alergeno = Alergeno.Frutos_Secos,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.5,
                grasas = 2.5,
                minerales = 0.8,
                vitaminas = 2.0,
                calorias = 13,
                foto = R.drawable.be_lechealmendra,
            ),

            Comida(
                nombre = "Leche de Soja",
                alergeno = Alergeno.Soja,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 3.3,
                grasas = 1.8,
                minerales = 0.6,
                vitaminas = 2.5,
                calorias = 33,
                foto = R.drawable.be_soja,
            ),

            Comida(
                nombre = "Té",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.0,
                grasas = 0.0,
                minerales = 0.5,
                vitaminas = 0.5,
                calorias = 1,
                foto = R.drawable.be_te,
            ),

            Comida(
                nombre = "Zumo de Manzana",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.1,
                grasas = 0.1,
                minerales = 0.3,
                vitaminas = 2.2,
                calorias = 46,
                foto = R.drawable.be_zumomanzana,
            ),

            Comida(
                nombre = "Zumo de Naranja",
                alergeno = Alergeno.Ninguno,
                categoria = Categoria.Bebida,
                bebida = true,
                proteinas = 0.7,
                grasas = 0.2,
                minerales = 0.1,
                vitaminas = 50.0,
                calorias = 45,
                foto = R.drawable.be_zumonaranja
            ),

            )

    }
}