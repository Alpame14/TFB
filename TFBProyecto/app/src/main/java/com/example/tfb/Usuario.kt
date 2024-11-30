package com.example.tfb

/*

 */
import com.example.tfb.Enumerados.ProviderType

data class Usuario(val nombre: String, val email: String, val provider: ProviderType, val maxscore: Int) {

    companion object {
        // Static variables or functions can be defined here
        var currentUsuario: Usuario? = null
        fun crearUsuarioInvitado(): Usuario {
            return Usuario("Invitado", "", ProviderType.NINGUNO, 0)
        }
        fun crearUsuario(nombre: String, email: String, provider: ProviderType, maxscore: Int): Usuario {
            return Usuario(nombre, email, provider, maxscore)
        }


    }

}

