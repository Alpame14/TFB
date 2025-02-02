package com.example.tfb

/*

 */
import android.provider.ContactsContract.CommonDataKinds.Photo
import com.example.tfb.Enumerados.ProviderType

data class Usuario(
    val nombre: String,
    val email: String,
    val provider: ProviderType,
    var maxscore: Int,
    val foto: String? = null  // Agregar foto como atributo opcional
) {
    companion object {
        var currentUsuario: Usuario? = null

        // Método para crear un nuevo usuario
        fun crearUsuario(nombre: String, email: String, provider: ProviderType, maxscore: Int, foto: String? = null): Usuario {
            return Usuario(nombre, email, provider, maxscore, foto)
        }

        fun crearUsuarioInvitado(): Usuario {
            return Usuario("Invitado", "", ProviderType.NINGUNO, 0)
        }
        fun crearUsuario(nombre: String, email: String, provider: ProviderType, maxscore: Int): Usuario {
            return Usuario(nombre, email, provider, maxscore)
        }
    }
}


