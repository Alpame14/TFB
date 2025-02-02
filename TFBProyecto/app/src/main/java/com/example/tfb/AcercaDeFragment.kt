package com.example.tfb

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.tfb.databinding.FragmentAcercaDeBinding

class AcercaDeFragment : Fragment() {

    private var _binding: FragmentAcercaDeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcercaDeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Habilitar menú en el fragmento
        setHasOptionsMenu(true)

        // Configurar la Toolbar como ActionBar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        // Cambiar la fuente del título de la Toolbar a Angkor
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.angkor)
        binding.toolbar.title = "¡Contacta con nosotros!"

        // Asegurar que el título adopte la fuente personalizada
        for (i in 0 until binding.toolbar.childCount) {
            val view = binding.toolbar.getChildAt(i)
            if (view is TextView) {
                view.typeface = typeface
                view.setTextColor(Color.parseColor("#EDE7DF")) // Color beige
                view.textSize = 20f
            }
        }

        // Configurar el VideoView
        val videoPath = "android.resource://${requireActivity().packageName}/raw/videoapp"
        val uri = Uri.parse(videoPath)
        binding.videoView.setVideoURI(uri)

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)

        // Reproducir automáticamente al cargar
        binding.videoView.setOnPreparedListener { it.start() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_acerca_de, menu)
        val menuItem = menu.findItem(R.id.action_contacto)
        val spannable = SpannableString(menuItem.title)
        spannable.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannable.length, 0)
        menuItem.title = spannable

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_contacto -> {
                abrirCorreo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun abrirCorreo() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("timeforbrunch@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Soporte - TimeForBrunch")
            putExtra(Intent.EXTRA_TEXT, "Hola, necesito ayuda con...")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo con:"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
