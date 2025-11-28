package com.dam.agendadam

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.databinding.ActivityRegistroBinding
import com.dam.agendadam.entity.Usuario
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    // View Binding para acceder a las vistas del XML de registro
    private lateinit var binding: ActivityRegistroBinding

    // DAO de Usuario: objeto para comunicarse con la tabla usuarios de Room
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Crear el binding y usarlo como vista raíz de la Activity
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la instancia de la BD y del UsuarioDao desde la clase Application
        val app = application as AgendaApp
        usuarioDao = app.getDatabase().usuarioDao()

        // Cuando se pulsa el botón "Registrar"
        binding.btnRegistrarUsuario.setOnClickListener {
            // Leer y limpiar los textos de los EditText
            val nombre = binding.etNombreUsuario.text.toString().trim()
            val email = binding.etEmailRegistro.text.toString().trim()
            val pass = binding.etPasswordRegistro.text.toString().trim()

            // Validar que no haya campos vacíos
            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Lanzar una corrutina ligada al ciclo de vida de la Activity
                lifecycleScope.launch {
                    // Insertar el nuevo usuario en la base de datos
                    usuarioDao.insertar(
                        Usuario(
                            nombreUsuario = nombre,
                            email = email,
                            password = pass
                        )
                    )
                    // Avisar al usuario y cerrar la pantalla de registro
                    Toast.makeText(
                        this@RegistroActivity,
                        "Usuario registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Volver a LoginActivity
                    finish()
                }
            }
        }

        binding.btnVolverLogin.setOnClickListener {
            finish()  // cierra RegistroActivity y vuelve a LoginActivity
        }

    }
}
