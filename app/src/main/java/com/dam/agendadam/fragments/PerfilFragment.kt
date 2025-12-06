package com.dam.agendadam.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dam.agendadam.AgendaApp
import com.dam.agendadam.LoginActivity
import com.dam.agendadam.R
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.databinding.FragmentPerfilBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de perfil.
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    // Id del usuario logueado (se rellena al cargar desde la BD).
    private var idUsuario: Int? = null

    // Copias en memoria del nombre y email del usuario logueado.
    private var nombreUsuario: String? = null
    private var emailUsuario: String? = null

    // Referencia al DAO de usuarios para acceder a Room.
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera los datos iniciales de usuario pasados al Fragment por argumentos.
        arguments?.let {
            nombreUsuario = it.getString("NOMBRE_USUARIO")
            emailUsuario = it.getString("EMAIL_USUARIO")
        }

        // Obtiene la instancia de la base de datos a través de la clase Application.
        val app = requireActivity().application as AgendaApp
        usuarioDao = app.getDatabase().usuarioDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout del fragmento usando ViewBinding.
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        // Carga/recarga el usuario desde la base de datos para tener datos actualizados.
        cargarUsuarioDesdeBd()

        // Botón para abrir el diálogo de edición de perfil.
        binding.btnEditarPerfil.setOnClickListener {
            mostrarDialogoEditarPerfil()
        }

        // Botón para cerrar sesión y volver a la pantalla de login.
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        return binding.root
    }

    // Obtiene el usuario desde Room usando id (si existe) o email inicial.
    private fun cargarUsuarioDesdeBd() {
        val id = idUsuario
        val emailInicial = emailUsuario

        viewLifecycleOwner.lifecycleScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                when {
                    // Si ya conocemos el id, es la forma más segura de recuperar al usuario.
                    id != null -> usuarioDao.obtenerPorId(id)
                    // Si no hay id, intentamos localizarlo por el email inicial.
                    emailInicial != null -> usuarioDao.obtenerPorEmail(emailInicial)
                    else -> null
                }
            }

            if (usuario != null) {
                // Si se ha encontrado en la BD, actualizamos id y datos en memoria y en la UI.
                idUsuario = usuario.id
                nombreUsuario = usuario.nombreUsuario
                emailUsuario = usuario.email
                binding.tvNombrePerfil.text = usuario.nombreUsuario
                binding.tvEmailPerfil.text = usuario.email
            } else {
                // Si no se encuentra en la BD, mostramos lo que tengamos en memoria.
                binding.tvNombrePerfil.text = nombreUsuario ?: ""
                binding.tvEmailPerfil.text = emailUsuario ?: ""
            }
        }
    }

    // Muestra un diálogo con dos EditText para editar nombre y email del usuario.
    private fun mostrarDialogoEditarPerfil() {
        val ctx = requireContext()
        val vistaDialogo = layoutInflater.inflate(R.layout.dialog_editar_perfil, null)

        val etNombre = vistaDialogo.findViewById<EditText>(R.id.etNombreEditar)
        val etEmail = vistaDialogo.findViewById<EditText>(R.id.etEmailEditar)

        // Rellena los campos con los valores actuales del usuario.
        etNombre.setText(nombreUsuario ?: "")
        etEmail.setText(emailUsuario ?: "")

        AlertDialog.Builder(ctx)
            .setTitle(getString(R.string.perfil_boton_editar))
            .setView(vistaDialogo)
            // Botón de guardar cambios.
            .setPositiveButton(R.string.perfil_guardar) { _, _ ->
                val nuevoNombre = etNombre.text.toString().trim()
                val nuevoEmail = etEmail.text.toString().trim()

                // Validación básica: no permitir campos vacíos.
                if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty()) {
                    Toast.makeText(ctx, "Nombre y email no pueden estar vacíos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Si los datos son válidos, se guardan en la base de datos.
                    guardarCambiosPerfil(nuevoNombre, nuevoEmail)
                }
            }
            // Botón de cancelar que simplemente cierra el diálogo.
            .setNegativeButton(R.string.perfil_cancelar, null)
            .show()
    }

    // Actualiza en Room el nombre y email del usuario y refresca la UI.
    private fun guardarCambiosPerfil(nuevoNombre: String, nuevoEmail: String) {
        val id = idUsuario
        val emailActual = emailUsuario

        lifecycleScope.launch {
            // Busca el usuario en la BD por id (si existe) o por email actual.
            val usuario = withContext(Dispatchers.IO) {
                if (id != null) {
                    usuarioDao.obtenerPorId(id)
                } else if (emailActual != null) {
                    usuarioDao.obtenerPorEmail(emailActual)
                } else {
                    null
                }
            }

            // Si no se encuentra, se avisa y se sale de la corrutina.
            if (usuario == null) {
                Toast.makeText(
                    requireContext(),
                    "No se ha encontrado el usuario en la base de datos",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            // Crea una copia del usuario con los nuevos datos de nombre y email.
            val usuarioActualizado = usuario.copy(
                nombreUsuario = nuevoNombre,
                email = nuevoEmail
            )

            // Actualiza el usuario en la base de datos.
            withContext(Dispatchers.IO) {
                usuarioDao.actualizar(usuarioActualizado)
            }

            // Actualiza también los datos en memoria y en la interfaz.
            idUsuario = usuarioActualizado.id
            nombreUsuario = usuarioActualizado.nombreUsuario
            emailUsuario = usuarioActualizado.email

            binding.tvNombrePerfil.text = usuarioActualizado.nombreUsuario
            binding.tvEmailPerfil.text = usuarioActualizado.email

            Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()

            // Vuelve a cargar desde la BD por si en el futuro se amplían los datos del usuario.
            cargarUsuarioDesdeBd()
        }
    }

    // Cierra la sesión actual borrando la información de "sesion" en SharedPreferences
    // y llevando al usuario de vuelta a la pantalla de login.
    private fun cerrarSesion() {
        val prefs = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Crea un Intent hacia LoginActivity limpiando el back stack
        // para que no se pueda volver atrás al perfil tras cerrar sesión.
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    // Limpia la referencia al binding cuando se destruye la vista para evitar fugas de memoria.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
