package com.dam.agendadam.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam.agendadam.AgendaApp
import com.dam.agendadam.R
import com.dam.agendadam.adapters.TareaAdapter
import com.dam.agendadam.dao.TareaDao
import com.dam.agendadam.databinding.FragmentTareasBinding
import com.dam.agendadam.entity.TareaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasFragment : Fragment() {

    private var _binding: FragmentTareasBinding? = null
    private val binding get() = _binding!!

    private lateinit var tareaDao: TareaDao
    private lateinit var adapter: TareaAdapter

    // usuario al que pertenecen las tareas que se muestran
    private var nombreUsuarioLogueado: String = ""

    private var tareaSeleccionada: TareaEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application as AgendaApp
        tareaDao = app.getDatabase().tareaDao()

        // viene desde MainActivity vía argumentos
        nombreUsuarioLogueado = arguments?.getString("NOMBRE_USUARIO") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTareasBinding.inflate(inflater, container, false)

        configurarSpinnerPrioridad()
        configurarRecyclerView()
        configurarBotones()
        configurarSearchView()
        configurarSeekBarUrgencia()
        observarTareas()   // carga inicial de tareas del usuario

        return binding.root
    }

    private fun configurarSpinnerPrioridad() {
        val prioridades = listOf(
            getString(R.string.prioridad_alta),
            getString(R.string.prioridad_media),
            getString(R.string.prioridad_baja)
        )

        val adapterSpinner = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            prioridades
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPrioridad.adapter = adapterSpinner
    }

    private fun configurarRecyclerView() {
        adapter = TareaAdapter(onItemClick = { tarea ->
            tareaSeleccionada = tarea
            rellenarFormulario(tarea)
        })
        binding.rvTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTareas.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnGuardarTarea.setOnClickListener {
            guardarOActualizarTarea()
        }
        binding.btnBorrarTarea.setOnClickListener {
            borrarTareaSeleccionada()
        }
        // NUEVO: botón para compartir
        binding.btnCompartirTarea.setOnClickListener {
            compartirTareaSeleccionada()
        }
    }

    private fun configurarSearchView() {
        binding.svBuscarTarea.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filtrarTareas(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarTareas(newText.orEmpty())
                return true
            }
        })
    }

    private fun configurarSeekBarUrgencia() {
        binding.tvValorUrgencia.text = binding.sbUrgencia.progress.toString()

        binding.sbUrgencia.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvValorUrgencia.text = progress.toString()

                // Cambiar prioridad según el valor del SeekBar
                val index = when {
                    progress >= 7 -> 0 // Alta / High
                    progress >= 4 -> 1 // Media / Medium
                    else -> 2          // Baja / Low
                }
                if (binding.spPrioridad.selectedItemPosition != index) {
                    binding.spPrioridad.setSelection(index)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }


    // Carga todas las tareas del usuario logueado
    private fun observarTareas() {
        viewLifecycleOwner.lifecycleScope.launch {
            val lista = withContext(Dispatchers.IO) {
                tareaDao.obtenerTareasDeUsuario(nombreUsuarioLogueado)
            }
            adapter.actualizarLista(lista)
        }
    }

    // Búsqueda siempre dentro de las tareas del usuario logueado
    private fun filtrarTareas(texto: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val lista = withContext(Dispatchers.IO) {
                if (texto.isBlank()) {
                    tareaDao.obtenerTareasDeUsuario(nombreUsuarioLogueado)
                } else {
                    tareaDao.buscarPorTituloDeUsuario(nombreUsuarioLogueado, texto)
                }
            }
            adapter.actualizarLista(lista)
        }
    }

    private fun rellenarFormulario(tarea: TareaEntity) {
        binding.etTituloTarea.setText(tarea.titulo)
        binding.etDescripcionTarea.setText(tarea.descripcion)

        val adapterSpinner = binding.spPrioridad.adapter as ArrayAdapter<String>
        val index = adapterSpinner.getPosition(tarea.prioridad)
        if (index >= 0) binding.spPrioridad.setSelection(index)

        binding.sbUrgencia.progress = tarea.urgente
        binding.tvValorUrgencia.text = tarea.urgente.toString()
        binding.chkCompletada.isChecked = tarea.completada

        adapter.seleccionarTarea(tarea)
    }

    private fun limpiarFormulario() {
        tareaSeleccionada = null
        binding.etTituloTarea.text.clear()
        binding.etDescripcionTarea.text.clear()
        binding.spPrioridad.setSelection(0)
        binding.sbUrgencia.progress = 0
        binding.tvValorUrgencia.text = "0"
        binding.chkCompletada.isChecked = false
    }

    private fun guardarOActualizarTarea() {
        val titulo = binding.etTituloTarea.text.toString().trim()
        val descripcion = binding.etDescripcionTarea.text.toString().trim()
        val prioridad = binding.spPrioridad.selectedItem.toString()
        val urgente = binding.sbUrgencia.progress
        val completada = binding.chkCompletada.isChecked

        if (titulo.isEmpty()) {
            Toast.makeText(requireContext(), "Introduce un título", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val tarea = tareaSeleccionada?.copy(
                    titulo = titulo,
                    descripcion = descripcion,
                    prioridad = prioridad,
                    urgente = urgente,
                    completada = completada,
                    nombreUsuario = nombreUsuarioLogueado
                ) ?: TareaEntity(
                    titulo = titulo,
                    descripcion = descripcion,
                    prioridad = prioridad,
                    urgente = urgente,
                    completada = completada,
                    nombreUsuario = nombreUsuarioLogueado
                )

                if (tareaSeleccionada == null) {
                    tareaDao.insertar(tarea)
                } else {
                    tareaDao.actualizar(tarea)
                }
            }

            observarTareas()
            limpiarFormulario()
            tareaSeleccionada = null
            adapter.limpiarSeleccion()
        }
    }

    private fun borrarTareaSeleccionada() {
        val tarea = tareaSeleccionada ?: run {
            Toast.makeText(
                requireContext(),
                "Selecciona una tarea de la lista",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                tareaDao.borrar(tarea)
            }

            observarTareas()
            limpiarFormulario()
            tareaSeleccionada = null
            adapter.limpiarSeleccion()
        }
    }

    private fun compartirTareaSeleccionada() {
        val tarea = tareaSeleccionada ?: run {
            Toast.makeText(
                requireContext(),
                "Selecciona una tarea para compartir",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val texto = "Título: ${tarea.titulo}\nDescripción: ${tarea.descripcion}"

        val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, texto)
        }

        val chooser = android.content.Intent.createChooser(sendIntent, "Compartir tarea")
        startActivity(chooser)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
