package com.dam.agendadam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dam.agendadam.databinding.ActivityMainBinding
import com.dam.agendadam.fragments.AjustesFragment
import com.dam.agendadam.fragments.ApiFragment
import com.dam.agendadam.fragments.PerfilFragment
import com.dam.agendadam.fragments.TareasFragment

class MainActivity : AppCompatActivity() {

    // ViewBinding para acceder a las vistas del layout principal.
    private lateinit var binding: ActivityMainBinding

    // Nombre del usuario que ha iniciado sesión y que se pasa a los fragments.
    private var nombreUsuarioLogueado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla el layout de la actividad principal.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recoger datos enviados desde LoginActivity.
        val nombre = intent.getStringExtra("nombreUsuario")
        val email = intent.getStringExtra("emailUsuario")

        // Guardar el nombre en la propiedad para reutilizarlo.
        nombreUsuarioLogueado = nombre ?: ""

        // Crear PerfilFragment con argumentos (nombre y email del usuario).
        val perfilFragment = PerfilFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombreUsuarioLogueado)
                putString("EMAIL_USUARIO", email)
            }
        }

        // Crear TareasFragment con el nombre del usuario.
        val tareasFragment = TareasFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombreUsuarioLogueado)
            }
        }

        // Crear AjustesFragment con el nombre del usuario para preferencias por usuario.
        val ajustesFragment = AjustesFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombreUsuarioLogueado)
            }
        }

        // Fragment inicial SOLO la primera vez que se crea la actividad.
        if (savedInstanceState == null) {
            replaceFragment(perfilFragment)
            binding.bottomNav.selectedItemId = R.id.nav_perfil
        }

        // Configuración del BottomNavigation para cambiar entre fragments.
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_perfil -> replaceFragment(perfilFragment)
                R.id.nav_tareas -> replaceFragment(tareasFragment)
                R.id.nav_api -> replaceFragment(ApiFragment())
                R.id.nav_ajustes -> replaceFragment(ajustesFragment)
            }
            true
        }
    }

    // Función de ayuda para reemplazar el fragment actual en el contenedor.
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
