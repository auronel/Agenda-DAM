package com.dam.agendadam.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.R
import com.dam.agendadam.adapters.TareaRemotaAdapter
import com.dam.agendadam.api.TareaApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiFragment : Fragment() {

    // RecyclerView que mostrará la lista de tareas remotas.
    private lateinit var recyclerView: RecyclerView

    // Adaptador que pinta cada tarea remota en el RecyclerView.
    private lateinit var adapter: TareaRemotaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento que contiene el RecyclerView.
        val view = inflater.inflate(R.layout.fragment_api, container, false)

        // Referencia al RecyclerView del layout.
        recyclerView = view.findViewById(R.id.rvTareasRemotas)
        // Crea el adaptador vacío al inicio.
        adapter = TareaRemotaAdapter()
        // Asigna un LinearLayoutManager para mostrar la lista en forma de columna.
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Conecta el adaptador con el RecyclerView.
        recyclerView.adapter = adapter

        // Lanza la carga de tareas desde la API al crear la vista.
        cargarTareasRemotas()

        return view
    }

    // Función que se encarga de llamar a la API y actualizar la lista.
    private fun cargarTareasRemotas() {
        // Usa lifecycleScope para lanzar una corrutina ligada al ciclo de vida del Fragment.
        lifecycleScope.launch {
            try {
                // Ejecuta la llamada de red en un hilo de IO para no bloquear la UI.
                val respuesta = withContext(Dispatchers.IO) {
                    TareaApiClient.service.obtenerTareas()
                }
                // Actualiza el adaptador con la lista de tareas recibidas.
                adapter.actualizarLista(respuesta.todos)
            } catch (e: Exception) {
                // Si hay cualquier error (sin conexión, fallo de servidor, etc.), muestra un Toast.
                Toast.makeText(
                    requireContext(),
                    "Error al cargar tareas de la API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
