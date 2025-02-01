package com.example.tfb

class Enumerados {

    enum class ProviderType{
        NINGUNO, BASIC, GOOGLE
    }
    enum class Alergeno{
        Huevo,
        Lacteo,
        Frutos_Secos,
        Gluten,
        Pescado,
        Soja,
        Crustaceos,
        Moluscos,
        Ninguno
    }
    enum class Dietas{
        Vegetariano,
        Vegano,
        Ninguna
    }
    enum class Categoria{
        Ninguno,
        Verdura,
        Fruta,
        Lacteo,
        Cereal,
        Animal,
        Bebida
    }
}