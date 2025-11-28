package com.dam.agendadam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // obtener DAO
        val app = application as AgendaApp
        usuarioDao = app.getDatabase().usuarioDao()

        // botón de login
        // En el click listener del login:
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val usuario = usuarioDao.login(email, pass)
                    if (usuario != null) {
                        // LOGIN CORRECTO: va a MainActivity y pasa el usuario
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("nombreUsuario", usuario.nombreUsuario)
                        intent.putExtra("emailUsuario", usuario.email)
                        startActivity(intent)
                        finish()
                    } else {
                        // Si el usuario no existe, mostrar error
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnIrRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

    }
}

