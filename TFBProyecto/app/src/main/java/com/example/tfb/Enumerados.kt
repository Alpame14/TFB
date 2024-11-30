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
    enum class Restricciones{
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
        Origen_Animal,
        Bebida
    }
}