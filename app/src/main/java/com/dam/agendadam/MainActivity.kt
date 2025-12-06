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

    private lateinit var binding: ActivityMainBinding

    private var nombreUsuarioLogueado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Recoger datos enviados desde LoginActivity
        val nombre = intent.getStringExtra("nombreUsuario")
        val email = intent.getStringExtra("emailUsuario")

        // Guardar el nombre en la propiedad
        nombreUsuarioLogueado = nombre ?: ""

        // 2) Crear PerfilFragment con argumentos
        val perfilFragment = PerfilFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombreUsuarioLogueado)
                putString("EMAIL_USUARIO", email)
            }
        }

        // 3) Crear TareasFragment con el nombre como argumento
        val tareasFragment = TareasFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombreUsuarioLogueado)
            }
        }

        // 4) Fragment inicial SOLO la primera vez
        if (savedInstanceState == null) {
            replaceFragment(perfilFragment)
            binding.bottomNav.selectedItemId = R.id.nav_perfil
        }

        // 5) BottomNavigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_perfil -> replaceFragment(perfilFragment)
                R.id.nav_tareas -> replaceFragment(tareasFragment)
                R.id.nav_api -> replaceFragment(ApiFragment())
                R.id.nav_ajustes -> replaceFragment(AjustesFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}