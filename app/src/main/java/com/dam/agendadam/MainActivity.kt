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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Recoger datos enviados desde LoginActivity
        val nombre = intent.getStringExtra("nombreUsuario")
        val email = intent.getStringExtra("emailUsuario")

        // 2) Crear PerfilFragment con argumentos
        val perfilFragment = PerfilFragment().apply {
            arguments = Bundle().apply {
                putString("NOMBRE_USUARIO", nombre)
                putString("EMAIL_USUARIO", email)
            }
        }

        // 3) Fragment inicial: perfil
        replaceFragment(perfilFragment)

        // 4) BottomNavigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_perfil -> replaceFragment(perfilFragment)
                R.id.nav_tareas -> replaceFragment(TareasFragment())
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
