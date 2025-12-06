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

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TareaRemotaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api, container, false)

        recyclerView = view.findViewById(R.id.rvTareasRemotas)
        adapter = TareaRemotaAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        cargarTareasRemotas()

        return view
    }

    private fun cargarTareasRemotas() {
        lifecycleScope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    TareaApiClient.service.obtenerTareas()
                }
                adapter.actualizarLista(respuesta.todos)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar tareas de la API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
