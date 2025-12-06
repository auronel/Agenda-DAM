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

    // ViewBinding para acceder a las vistas del layout de tareas.
    private var _binding: FragmentTareasBinding? = null
    private val binding get() = _binding!!

    // DAO de tareas para acceder a la base de datos Room.
    private lateinit var tareaDao: TareaDao

    // Adaptador del RecyclerView que muestra las tareas.
    private lateinit var adapter: TareaAdapter

    // Usuario al que pertenecen las tareas que se muestran.
    private var nombreUsuarioLogueado: String = ""

    // Referencia a la tarea actualmente seleccionada en la lista (si hay).
    private var tareaSeleccionada: TareaEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtiene la base de datos desde la clase Application y el DAO de tareas.
        val app = requireActivity().application as AgendaApp
        tareaDao = app.getDatabase().tareaDao()

        // Recupera el nombre de usuario enviado desde MainActivity por argumentos.
        nombreUsuarioLogueado = arguments?.getString("NOMBRE_USUARIO") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout del fragmento de tareas usando ViewBinding.
        _binding = FragmentTareasBinding.inflate(inflater, container, false)

        // Configura los distintos componentes de la interfaz.
        configurarSpinnerPrioridad()
        configurarRecyclerView()
        configurarBotones()
        configurarSearchView()
        configurarSeekBarUrgencia()
        // Carga inicial de las tareas del usuario logueado.
        observarTareas()

        return binding.root
    }

    // Configura el Spinner de prioridad con textos obtenidos de recursos (multiidioma).
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

    // Configura el RecyclerView y el adaptador de tareas locales.
    private fun configurarRecyclerView() {
        adapter = TareaAdapter(onItemClick = { tarea ->
            // Guarda la tarea seleccionada y rellena el formulario con sus datos.
            tareaSeleccionada = tarea
            rellenarFormulario(tarea)
        })
        binding.rvTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTareas.adapter = adapter
    }

    // Asigna la lógica de los botones: guardar, borrar y compartir tarea.
    private fun configurarBotones() {
        binding.btnGuardarTarea.setOnClickListener {
            guardarOActualizarTarea()
        }
        binding.btnBorrarTarea.setOnClickListener {
            borrarTareaSeleccionada()
        }
        // Botón para compartir la tarea seleccionada mediante un Intent.
        binding.btnCompartirTarea.setOnClickListener {
            compartirTareaSeleccionada()
        }
    }

    // Configura el SearchView para filtrar las tareas por título del usuario logueado.
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

    // Configura el SeekBar de urgencia y sincroniza su valor con el texto y la prioridad.
    private fun configurarSeekBarUrgencia() {
        // Muestra el valor inicial de urgencia.
        binding.tvValorUrgencia.text = binding.sbUrgencia.progress.toString()

        binding.sbUrgencia.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                // Actualiza el texto que muestra el valor de urgencia.
                binding.tvValorUrgencia.text = progress.toString()

                // Cambia la prioridad automáticamente según el valor del SeekBar.
                val index = when {
                    progress >= 7 -> 0 // Alta / High
                    progress >= 4 -> 1 // Media / Medium
                    else -> 2          // Baja / Low
                }
                // Solo cambia la selección del Spinner si es diferente para evitar bucles.
                if (binding.spPrioridad.selectedItemPosition != index) {
                    binding.spPrioridad.setSelection(index)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    // Carga todas las tareas del usuario logueado desde la base de datos.
    private fun observarTareas() {
        lifecycleScope.launch {
            val lista = withContext(Dispatchers.IO) {
                tareaDao.obtenerTareasDeUsuario(nombreUsuarioLogueado)
            }
            adapter.actualizarLista(lista)
        }
    }

    // Filtra las tareas solo dentro de las del usuario logueado, según el texto introducido.
    private fun filtrarTareas(texto: String) {
        lifecycleScope.launch {
            val lista = withContext(Dispatchers.IO) {
                if (texto.isBlank()) {
                    // Si no hay texto, se cargan todas las tareas del usuario.
                    tareaDao.obtenerTareasDeUsuario(nombreUsuarioLogueado)
                } else {
                    // Si hay texto, se filtra por título para ese usuario.
                    tareaDao.buscarPorTituloDeUsuario(nombreUsuarioLogueado, texto)
                }
            }
            adapter.actualizarLista(lista)
        }
    }

    // Rellena el formulario con los datos de la tarea seleccionada.
    private fun rellenarFormulario(tarea: TareaEntity) {
        binding.etTituloTarea.setText(tarea.titulo)
        binding.etDescripcionTarea.setText(tarea.descripcion)

        // Coloca en el Spinner la prioridad correspondiente a la tarea.
        val adapterSpinner = binding.spPrioridad.adapter as ArrayAdapter<String>
        val index = adapterSpinner.getPosition(tarea.prioridad)
        if (index >= 0) binding.spPrioridad.setSelection(index)

        // Ajusta el SeekBar y el texto de urgencia.
        binding.sbUrgencia.progress = tarea.urgente
        binding.tvValorUrgencia.text = tarea.urgente.toString()
        // Marca o desmarca el check de completada.
        binding.chkCompletada.isChecked = tarea.completada

        // Indica al adaptador qué tarea está seleccionada para resaltar el ítem.
        adapter.seleccionarTarea(tarea)
    }

    // Limpia todos los campos del formulario y quita la selección actual.
    private fun limpiarFormulario() {
        tareaSeleccionada = null
        binding.etTituloTarea.text.clear()
        binding.etDescripcionTarea.text.clear()
        binding.spPrioridad.setSelection(0)
        binding.sbUrgencia.progress = 0
        binding.tvValorUrgencia.text = "0"
        binding.chkCompletada.isChecked = false
    }

    // Crea una nueva tarea o actualiza la seleccionada, según corresponda.
    private fun guardarOActualizarTarea() {
        val titulo = binding.etTituloTarea.text.toString().trim()
        val descripcion = binding.etDescripcionTarea.text.toString().trim()
        val prioridad = binding.spPrioridad.selectedItem.toString()
        val urgente = binding.sbUrgencia.progress
        val completada = binding.chkCompletada.isChecked

        // Validación básica: el título es obligatorio.
        if (titulo.isEmpty()) {
            Toast.makeText(requireContext(), "Introduce un título", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // Si hay tarea seleccionada, se crea una copia modificada; si no, una nueva tarea.
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

                // Inserta o actualiza según corresponda.
                if (tareaSeleccionada == null) {
                    tareaDao.insertar(tarea)
                } else {
                    tareaDao.actualizar(tarea)
                }
            }

            // Tras guardar, recarga la lista, limpia el formulario y quita la selección.
            observarTareas()
            limpiarFormulario()
            tareaSeleccionada = null
            adapter.limpiarSeleccion()
        }
    }

    // Elimina de la base de datos la tarea actualmente seleccionada.
    private fun borrarTareaSeleccionada() {
        val tarea = tareaSeleccionada ?: run {
            Toast.makeText(
                requireContext(),
                "Selecciona una tarea de la lista",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                tareaDao.borrar(tarea)
            }

            // Tras borrar, recarga la lista, limpia el formulario y quita la selección.
            observarTareas()
            limpiarFormulario()
            tareaSeleccionada = null
            adapter.limpiarSeleccion()
        }
    }

    // Comparte la tarea seleccionada usando un Intent ACTION_SEND.
    private fun compartirTareaSeleccionada() {
        val tarea = tareaSeleccionada ?: run {
            Toast.makeText(
                requireContext(),
                "Selecciona una tarea para compartir",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Texto que se va a compartir con otras aplicaciones.
        val texto = "Título: ${tarea.titulo}\nDescripción: ${tarea.descripcion}"

        val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, texto)
        }

        // Muestra el chooser para que el usuario elija la app con la que quiere compartir.
        val chooser = android.content.Intent.createChooser(sendIntent, "Compartir tarea")
        startActivity(chooser)
    }

    // Limpia el binding cuando se destruye la vista para evitar fugas de memoria.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
