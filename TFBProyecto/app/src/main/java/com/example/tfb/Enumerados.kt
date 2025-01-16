package com.example.tfb

class Enumerados {

    enum class ProviderType{
        NINGUNO, BASIC, GOOGLE
    }
    enum class Alergeno{
        Huevo,
        Lacteos,
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
        alta_en_proteina,
        baja_en_grasas,
        Ninguna
    }
    enum class Categoria{
        Ninguno,
        Verdura,
        Fruta,
        Lacteo,
        Cereal,
        Origen_Animal,
        Bebida
    }
}