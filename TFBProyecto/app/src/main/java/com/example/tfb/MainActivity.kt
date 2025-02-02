package com.example.tfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tfb.databinding.ActivityMainBinding
import com.example.tfb.Enumerados.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        session()
        //binding.tv.text = Usuario.currentUsuario?.nombre ?: "Usuario no disponible"
       // binding.tv2.text = Usuario.currentUsuario?.email ?: "Email no disponible"
       // binding.tv3.text = Usuario.currentUsuario?.provider.toString()

        // Cargar animaciones
        val scaleRotateIn = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_in)
        val scaleRotateOut = AnimationUtils.loadAnimation(this, R.anim.scale_rotate_out)

        // Añadir clic y animación a ivPerfil
        binding.ivPerfil.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.startAnimation(scaleRotateOut)
            it.postDelayed({
                if (Usuario.currentUsuario != null && Usuario.currentUsuario!!.provider == ProviderType.NINGUNO) {
                    val intent = Intent(this, Registro::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, Cuenta::class.java)
                    startActivity(intent)
                }
            }, scaleRotateIn.duration)
        }

        // Añadir animaciones a las demás imágenes
        binding.ivAyuda.setOnClickListener {
            it.startAnimation(scaleRotateIn)
            it.postDelayed({
                val intent = Intent(this, MainAyuda::class.java)
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

                if (Usuario.currentUsuario?.provider == ProviderType.NINGUNO) {
                    Toast.makeText(
                        this,"Jugando sin registrar, la puntuación se guardará en local",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "La puntuación se guardará en el usuario ${Usuario.currentUsuario?.nombre}",Toast.LENGTH_LONG).show()
                }

                val intent = Intent(this, Jugar::class.java)
                startActivity(intent)
            }, scaleRotateIn.duration)
        }
    }



    private fun session() {
        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val nombre: String? = prefs.getString("nombre", null)
        val email: String? = prefs.getString("email", null)
        val provider: String? = prefs.getString("provider", null)

        if (email != null && provider != null && nombre != null) {


            Usuario.currentUsuario = Usuario.crearUsuario(
                nombre = nombre,
                email = email,
                provider = ProviderType.valueOf(provider),
                maxscore = 0)


        } else {
            Usuario.currentUsuario = Usuario.crearUsuarioInvitado()
        }
    }


}