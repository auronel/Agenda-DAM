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

    // Referencias a los CheckBox de la vista (notificaciones y tema oscuro).
    private lateinit var checkNotificaciones: CheckBox
    private lateinit var checkTemaOscuro: CheckBox

    // Nombre del usuario que ha iniciado sesión, para guardar preferencias por usuario.
    private var nombreUsuarioLogueado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera el nombre de usuario que viene en los argumentos del Fragment.
        nombreUsuarioLogueado = arguments?.getString("NOMBRE_USUARIO") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento de ajustes.
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        // Asocia las vistas del layout con las variables del Fragment.
        checkNotificaciones = view.findViewById(R.id.checkNotificaciones)
        checkTemaOscuro = view.findViewById(R.id.checkTemaOscuro)

        // Obtiene el objeto SharedPreferences donde se guardan los ajustes de la app.
        val prefs = requireContext()
            .getSharedPreferences("ajustes_app", Context.MODE_PRIVATE)

        // Prefijo que identifica a este usuario concreto.
        val prefijo = nombreUsuarioLogueado

        // Cargar preferencias SOLO de este usuario: notificaciones y tema oscuro.
        val notificacionesActivas =
            prefs.getBoolean("${prefijo}_PREF_NOTIFICACIONES", true)
        val temaOscuroActivo =
            prefs.getBoolean("${prefijo}_PREF_TEMA_OSCURO", false)

        // Aplica los valores cargados a los CheckBox.
        checkNotificaciones.isChecked = notificacionesActivas
        checkTemaOscuro.isChecked = temaOscuroActivo

        // Guarda la preferencia de notificaciones cuando el usuario cambie el CheckBox.
        checkNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean("${prefijo}_PREF_NOTIFICACIONES", isChecked)
                .apply()
        }

        // Guarda la preferencia de tema oscuro cuando el usuario cambie el CheckBox.
        checkTemaOscuro.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean("${prefijo}_PREF_TEMA_OSCURO", isChecked)
                .apply()
        }

        // Devuelve la vista inflada para que el sistema la muestre.
        return view
    }
}
