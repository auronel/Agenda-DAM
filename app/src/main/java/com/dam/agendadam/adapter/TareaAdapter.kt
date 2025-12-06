package com.dam.agendadam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.adapter.TareaViewHolder
import com.dam.agendadam.databinding.ItemTareaBinding
import com.dam.agendadam.entity.TareaEntity

class TareaAdapter(
    private var tareas: List<TareaEntity> = emptyList(),
    private val onItemClick: (TareaEntity) -> Unit
) : RecyclerView.Adapter<TareaViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun actualizarLista(nuevaLista: List<TareaEntity>) {
        tareas = nuevaLista
        notifyDataSetChanged()
    }

    fun seleccionarTarea(tarea: TareaEntity) {
        val oldPos = selectedPosition
        selectedPosition = tareas.indexOfFirst { it.id == tarea.id }
        if (oldPos != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPos)
        }
        if (selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition)
        }
    }

    fun limpiarSeleccion() {
//        val oldPos = selectedPosition
//        selectedPosition = RecyclerView.NO_POSITION
//        if (oldPos != RecyclerView.NO_POSITION) {
//            notifyItemChanged(oldPos)
//        }
        if (selectedPosition != RecyclerView.NO_POSITION) {
            val oldPos = selectedPosition
            selectedPosition = RecyclerView.NO_POSITION
            notifyItemChanged(oldPos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemTareaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TareaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        val isSelected = position == selectedPosition
        holder.bind(tarea, isSelected)
        holder.itemView.setOnClickListener {
            onItemClick(tarea)
            seleccionarTarea(tarea)
        }
    }

    override fun getItemCount(): Int = tareas.size
}
