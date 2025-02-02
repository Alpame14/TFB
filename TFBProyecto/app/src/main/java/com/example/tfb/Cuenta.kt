package com.example.tfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tfb.databinding.ActivityCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class Cuenta : AppCompatActivity() {
    private lateinit var binding: ActivityCuentaBinding
    private lateinit var storageFoto: StorageFoto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageFoto = StorageFoto(this, binding.imageView)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", "") ?: ""
        val nombre = prefs.getString("nombre", "Usuario") ?: "Usuario"
        val maxscore = prefs.getInt("maxscore", 0)  // 🔹 Cargar maxscore de SharedPreferences
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val storageRef = FirebaseStorage.getInstance().reference.child("imagenes_perfil/"+"perfil_${userId}.jpg")

// Obtener la URL de descarga de la foto
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val fotoUrl = uri.toString()  // Obtener la URL de la foto

            // Cargar la imagen con Glide si la URL no está vacía
            if (!fotoUrl.isNullOrEmpty()) {
                Glide.with(this)  // Usando Glide para cargar la imagen
                    .load(fotoUrl)
                    .into(binding.imageView)  // O el ImageView correspondiente
            } else {
                Toast.makeText(this, "La URL de la foto está vacía", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            // Mostrar el mensaje de error más detallado
            Log.e("FotoCarga", "Error al cargar la foto desde Firebase: ${e.message}")
            Toast.makeText(this, "Error al cargar la foto: ${e.message}", Toast.LENGTH_SHORT).show()

            // Puedes probar con una foto predeterminada o mostrar una imagen en caso de error
            Glide.with(this)
                .load(R.drawable.ico_perfil)  // Asegúrate de tener una imagen predeterminada en los recursos
                .into(binding.imageView)
        }
        // 🔹 Mostrar datos en la UI
        binding.usuariotxt.text = nombre
        binding.emailtxt.text = email
        binding.scoretxt.text = maxscore.toString()

        // 🔹 También actualizar Usuario.currentUsuario si es nulo
        if (Usuario.currentUsuario == null) {
            Usuario.currentUsuario =
                Usuario.crearUsuario(nombre, email, Enumerados.ProviderType.BASIC, maxscore)
        }

        // Botón de cerrar sesión
        binding.logoutbtn.setOnClickListener {
            cerrarSesion()
        }

        binding.imageView.setOnClickListener {
            storageFoto.abrirGaleria(this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Delegar el resultado a StorageFoto
        storageFoto.manejarResultado(requestCode, resultCode, data)
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val prefs: SharedPreferences.Editor =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
