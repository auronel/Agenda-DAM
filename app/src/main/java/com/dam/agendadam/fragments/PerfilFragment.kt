package com.dam.agendadam.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dam.agendadam.AgendaApp
import com.dam.agendadam.R
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.databinding.FragmentPerfilBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private var nombreUsuario: String? = null
    private var emailUsuario: String? = null

    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            nombreUsuario = it.getString("NOMBRE_USUARIO")
            emailUsuario = it.getString("EMAIL_USUARIO")
        }

        val app = requireActivity().application as AgendaApp
        usuarioDao = app.getDatabase().usuarioDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        binding.tvNombrePerfil.text = nombreUsuario ?: ""
        binding.tvEmailPerfil.text = emailUsuario ?: ""

        binding.btnEditarPerfil.setOnClickListener {
            mostrarDialogoEditarPerfil()
        }

        return binding.root
    }

    private fun mostrarDialogoEditarPerfil() {
        val ctx = requireContext()
        val vistaDialogo = layoutInflater.inflate(R.layout.dialog_editar_perfil, null)

        val etNombre = vistaDialogo.findViewById<EditText>(R.id.etNombreEditar)
        val etEmail = vistaDialogo.findViewById<EditText>(R.id.etEmailEditar)

        etNombre.setText(nombreUsuario ?: "")
        etEmail.setText(emailUsuario ?: "")

        AlertDialog.Builder(ctx)
            .setTitle(getString(R.string.perfil_boton_editar))
            .setView(vistaDialogo)
            .setPositiveButton(R.string.perfil_guardar) { _, _ ->
                val nuevoNombre = etNombre.text.toString().trim()
                val nuevoEmail = etEmail.text.toString().trim()

                if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty()) {
                    Toast.makeText(ctx, "Nombre y email no pueden estar vacíos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    guardarCambiosPerfil(nuevoNombre, nuevoEmail)
                }
            }
            .setNegativeButton(R.string.perfil_cancelar, null)
            .show()
    }

    private fun guardarCambiosPerfil(nuevoNombre: String, nuevoEmail: String) {
        val emailActual = emailUsuario ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                usuarioDao.obtenerPorEmail(emailActual)
            }

            if (usuario == null) {
                Toast.makeText(
                    requireContext(),
                    "No se ha encontrado el usuario en la base de datos",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            val usuarioActualizado = usuario.copy(
                nombreUsuario = nuevoNombre,
                email = nuevoEmail
            )

            withContext(Dispatchers.IO) {
                usuarioDao.actualizar(usuarioActualizado)
            }

            nombreUsuario = nuevoNombre
            emailUsuario = nuevoEmail
            binding.tvNombrePerfil.text = nuevoNombre
            binding.tvEmailPerfil.text = nuevoEmail

            Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
