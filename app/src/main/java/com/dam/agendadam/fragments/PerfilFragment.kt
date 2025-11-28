package com.dam.agendadam.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dam.agendadam.R

class PerfilFragment : Fragment() {

    private var nombreUsuario: String? = null
    private var emailUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nombreUsuario = it.getString("NOMBRE_USUARIO")
            emailUsuario = it.getString("EMAIL_USUARIO")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        val tvNombre = view.findViewById<TextView>(R.id.tvNombrePerfil)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmailPerfil)

        tvNombre.text = nombreUsuario ?: ""
        tvEmail.text = emailUsuario ?: ""

        return view
    }
}
