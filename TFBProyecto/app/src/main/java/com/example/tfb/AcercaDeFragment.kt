package com.example.tfb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.Toast
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

        // Configurar el botón para abrir el correo
        binding.btnCorreo.setOnClickListener {
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

        // Configurar el VideoView
        val videoPath = "android.resource://${requireActivity().packageName}/raw/videoapp"
        val uri = Uri.parse(videoPath)
        binding.videoView.setVideoURI(uri)

        // 🔥 Ajustar el MediaController para que quede bien dentro del contenedor 🔥
        val mediaController = MediaController(requireContext())
        val videoContainer = view.findViewById<FrameLayout>(R.id.videoContainer)

        mediaController.setAnchorView(binding.videoView) // Ahora se ajustará mejor al video
        binding.videoView.setMediaController(mediaController)

        // Reproducir automáticamente al cargar
        binding.videoView.setOnPreparedListener { it.start() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
