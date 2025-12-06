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

    // ViewBinding para acceder a las vistas del layout de login.
    private lateinit var binding: ActivityLoginBinding

    // DAO de usuarios para consultar la base de datos (login).
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla el layout de la actividad usando ViewBinding.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene el DAO de usuarios desde la base de datos de la aplicación.
        val app = application as AgendaApp
        usuarioDao = app.getDatabase().usuarioDao()

        // Configura el botón de login.
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            // Validación básica: no permitir campos vacíos.
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Lanza una corrutina para hacer la consulta a Room sin bloquear la UI.
                lifecycleScope.launch {
                    val usuario = usuarioDao.login(email, pass)
                    if (usuario != null) {
                        // Si el login es correcto, navega a MainActivity pasando nombre y email.
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("nombreUsuario", usuario.nombreUsuario)
                        intent.putExtra("emailUsuario", usuario.email)
                        startActivity(intent)
                        // Cierra la actividad de login para que no se pueda volver atrás.
                        finish()
                    } else {
                        // Si no hay coincidencia en la BD, muestra un mensaje de error.
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Botón para ir a la pantalla de registro de nuevo usuario.
        binding.btnIrRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
