package com.example.tfb

/*

 */
import android.provider.ContactsContract.CommonDataKinds.Photo
import com.example.tfb.Enumerados.ProviderType
import com.google.firebase.firestore.PropertyName

data class Usuario(
    @PropertyName("username") var nombre: String = "",  // Cambiar de `val` a `var`
    var email: String = "",
    var provider: ProviderType = ProviderType.NINGUNO,
    @PropertyName("maxscore") var maxscore: Int = 0,
    var foto: String? = null
) {
    constructor() : this("", "", ProviderType.NINGUNO, 0, null) // Constructor vacío obligatorio

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


