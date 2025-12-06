package com.dam.agendadam.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.dam.agendadam.R

class AjustesFragment : Fragment() {

    private lateinit var checkNotificaciones: CheckBox
    private lateinit var checkTemaOscuro: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        checkNotificaciones = view.findViewById(R.id.checkNotificaciones)
        checkTemaOscuro = view.findViewById(R.id.checkTemaOscuro)

        // Cargar preferencias guardadas
        val prefs = requireContext()
            .getSharedPreferences("ajustes_app", Context.MODE_PRIVATE)

        val notificacionesActivas = prefs.getBoolean("PREF_NOTIFICACIONES", true)
        val temaOscuroActivo = prefs.getBoolean("PREF_TEMA_OSCURO", false)

        checkNotificaciones.isChecked = notificacionesActivas
        checkTemaOscuro.isChecked = temaOscuroActivo

        // Guardar cuando el usuario cambie
        checkNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("PREF_NOTIFICACIONES", isChecked).apply()
        }

        checkTemaOscuro.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("PREF_TEMA_OSCURO", isChecked).apply()
        }

        return view
    }
}
