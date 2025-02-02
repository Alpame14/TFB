package com.example.tfb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class StorageFoto(private val context: Context, private val imageView: ImageView) {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("imagenes_perfil")
    // Abrir galería
    fun abrirGaleria(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    // Manejar el resultado de la selección
    fun manejarResultado(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)  // Actualiza el ImageView
            subirImagenFirebase()
        }
    }

    private fun actualizarUsuarioConFoto(fotoUrl: String) {
        val prefs = context.getSharedPreferences( // Usa context.getSharedPreferences
            context.getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        ).edit()

        prefs.putString("foto", fotoUrl)  // Guardar la URL de la foto en SharedPreferences
        prefs.apply()

        // También actualizar Usuario.currentUsuario
        if (Usuario.currentUsuario != null) {
            Usuario.currentUsuario = Usuario.currentUsuario?.copy(foto = fotoUrl)
        }

        // Actualizar la UI (por ejemplo, mostrar la foto en el ImageView)
        Glide.with(context)  // Usando Glide para cargar la imagen
            .load(fotoUrl)
            .into(imageView)
    }




    // Subir imagen a Firebase
    private fun subirImagenFirebase() {
        if (imageUri != null) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                // Usar el uid del usuario para guardar su foto de perfil con un nombre único
                val fileRef = storageRef.child("perfil_${userId}.jpg")

                fileRef.putFile(imageUri!!)
                    .addOnSuccessListener {
                        // Obtener la URL de la imagen subida
                        fileRef.downloadUrl.addOnSuccessListener { uri ->
                            Toast.makeText(context, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()

                            // Actualizar la foto en Firestore con la URL obtenida
                            val db = FirebaseFirestore.getInstance()
                            val userRef = db.collection("users").document(userId)

                            // Actualizar la URL de la foto en Firestore
                            userRef.update("foto", uri.toString())
                                .addOnSuccessListener {
                                    Log.d("Firebase", "Foto actualizada correctamente en Firestore")
                                    // Actualizar el usuario en la app con la URL de la foto
                                    actualizarUsuarioConFoto(uri.toString())
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error al actualizar la foto: ${e.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("Firebase", "Error al actualizar la foto", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Firebase", "Error al subir la imagen", e)
                    }
            }
        } else {
            Toast.makeText(context, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
