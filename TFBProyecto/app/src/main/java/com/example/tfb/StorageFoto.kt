package com.example.tfb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.tfb.databinding.ActivityCuentaBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class StorageFoto(private val context: Context, private val imageView: ImageView) {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private val storageRef: StorageReference =
        FirebaseStorage.getInstance().reference.child("imagenes_perfil")

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

    // Subir imagen a Firebase
    private fun subirImagenFirebase() {
        if (imageUri != null) {
            val fileRef = storageRef.child("perfil_${System.currentTimeMillis()}.jpg")

            fileRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        Toast.makeText(context, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                        Log.d("Firebase", "URL de la imagen: $uri")
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Firebase", "Error al subir la imagen", e)
                }
        } else {
            Toast.makeText(context, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
