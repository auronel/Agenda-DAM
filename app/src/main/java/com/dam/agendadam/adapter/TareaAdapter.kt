package com.dam.agendadam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.adapter.TareaViewHolder
import com.dam.agendadam.databinding.ItemTareaBinding
import com.dam.agendadam.entity.TareaEntity

// TareaAdapter es el adaptador que conecta la lista de tareas con el RecyclerView.
// Recibe una lista de TareaEntity y una función que se ejecuta al hacer clic en un ítem.
class TareaAdapter(
    // Lista de tareas que se va a mostrar en el RecyclerView.
    private var tareas: List<TareaEntity> = emptyList(),
    // Lambda que se llama cuando se pulsa una tarea (para notificar al Fragment/Activity).
    private val onItemClick: (TareaEntity) -> Unit
) : RecyclerView.Adapter<TareaViewHolder>() {

    // Guarda la posición actualmente seleccionada en la lista, o NO_POSITION si no hay ninguna.
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    // Actualiza la lista completa de tareas y avisa al RecyclerView para que se redibuje.
    fun actualizarLista(nuevaLista: List<TareaEntity>) {
        tareas = nuevaLista
        notifyDataSetChanged()
    }

    // Marca una tarea como seleccionada y actualiza visualmente la anterior y la nueva selección.
    fun seleccionarTarea(tarea: TareaEntity) {
        // Guarda la antigua posición seleccionada.
        val oldPos = selectedPosition
        // Busca la posición de la nueva tarea seleccionada comparando por id.
        selectedPosition = tareas.indexOfFirst { it.id == tarea.id }
        // Si había una posición seleccionada antes, se fuerza a redibujar ese ítem.
        if (oldPos != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPos)
        }
        // Si la nueva tarea existe en la lista, se redibuja para aplicar el estilo de seleccionado.
        if (selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition)
        }
    }

    // Limpia la selección actual, quitando el estilo de seleccionado en el ítem correspondiente.
    fun limpiarSeleccion() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            // Guarda la posición que estaba seleccionada para actualizar solo ese ítem.
            val oldPos = selectedPosition
            // Indica que ya no hay ningún elemento seleccionado.
            selectedPosition = RecyclerView.NO_POSITION
            // Notifica que el ítem ha cambiado para que el ViewHolder quite el estilo de selección.
            notifyItemChanged(oldPos)
        }
    }

    // Crea un nuevo ViewHolder inflando el layout de la tarea.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        // Infla el layout usando ViewBinding para obtener las vistas de item_tarea.xml.
        val binding = ItemTareaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Devuelve un TareaViewHolder preparado con el binding.
        return TareaViewHolder(binding)
    }

    // Enlaza los datos de una tarea concreta con el ViewHolder.
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        // Obtiene la tarea correspondiente a esta posición en la lista.
        val tarea = tareas[position]
        // Comprueba si esta posición es la que está actualmente seleccionada.
        val isSelected = position == selectedPosition
        // Llama al método bind del ViewHolder, pasándole la tarea y si está seleccionada o no.
        holder.bind(tarea, isSelected)
        // Configura el click del ítem para notificar al exterior y actualizar la selección.
        holder.itemView.setOnClickListener {
            // Notifica al Fragment/Activity qué tarea se ha pulsado.
            onItemClick(tarea)
            // Actualiza la selección interna del adaptador y fuerza el redibujado de los ítems afectados.
            seleccionarTarea(tarea)
        }
    }

    // Indica cuántos elementos tiene la lista; el RecyclerView usará este número de filas.
    override fun getItemCount(): Int = tareas.size
}
