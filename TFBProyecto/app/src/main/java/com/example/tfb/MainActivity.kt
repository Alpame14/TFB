package com.example.tfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.databinding.ActivityMainBinding


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
    Crustaceo,
    Moluscos,
    Ninguno
}
enum class Categoria{
    Vegetariano,
    Vegano,
    Frugívoro,
    Omnívoro
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        session()
       
        // Cargar animaciones
        val scaleRotateIn = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_in)
        val scaleRotateOut = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_out)

        // Añadir clic y animación a ivPerfil
        binding.ivPerfil.setOnClickListener {
            // Inicia la animación
            it.startAnimation(scaleRotateIn)
            it.startAnimation(scaleRotateOut)

            // Espera a que termine la animación antes de iniciar la actividad
            it.postDelayed({

                if (Usuario.currentUsuario!!.provider == ProviderType.NINGUNO) {
                    val intent = Intent(this, Registro::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, Cuenta::class.java)
                    startActivity(intent)
                }
            }, scaleRotateIn.duration)
        }

        // Añadir animaciones a las demás imágenes
        binding.ivAyuda.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Ayuda::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }

        binding.ivRanking.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Ranking::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }

        binding.ivJugar.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, Jugar::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }
    }

    //metodo que mira si la cuenta ya estaba abierta o no
    private fun session() {
        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nombre: String? = prefs.getString("nombre",null)
        val email: String? = prefs.getString("email",null)
        val provider: String? = prefs.getString("provider",null)

        if (email != null && provider != null && nombre != null){

            Usuario.currentUsuario = Usuario.crearUsuario(nombre,email,ProviderType.valueOf(provider),0)
        }
        else{
            Usuario.currentUsuario = Usuario.crearUsuarioInvitado()
        }

    }

}
// Esto es un cambio de prueba