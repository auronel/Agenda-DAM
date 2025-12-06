package com.dam.agendadam.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.R
import com.dam.agendadam.databinding.ItemTareaBinding
import com.dam.agendadam.entity.TareaEntity

// ViewHolder que se encarga de mostrar los datos de una TareaEntity en el item del RecyclerView.
class TareaViewHolder(
    private val binding: ItemTareaBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Recibe una tarea y un flag que indica si este ítem está seleccionado para aplicar estilos.
    fun bind(tarea: TareaEntity, isSelected: Boolean) {
        val context = binding.root.context

        // Asigna título y descripción a los TextView del layout.
        binding.tvTituloItem.text = tarea.titulo
        binding.tvDescripcionItem.text = tarea.descripcion

        // Obtiene los textos de las etiquetas desde resources (multiidioma).
        val prioridadLabel = context.getString(R.string.tarea_item_prioridad)
        val urgenciaLabel = context.getString(R.string.tarea_item_urgencia)
        val pendiente = context.getString(R.string.tarea_estado_pendiente)
        val completada = context.getString(R.string.tarea_estado_completada)

        // Decide el texto de estado según si la tarea está completada o no.
        val estado = if (tarea.completada) completada else pendiente

        // Construye la línea extra con prioridad, urgencia y estado.
        binding.tvInfoExtraItem.text =
            "$prioridadLabel ${tarea.prioridad} | $urgenciaLabel ${tarea.urgente} | $estado"

        // Cambia colores de fondo y texto si el ítem está seleccionado.
        if (isSelected) {
            val bg = ContextCompat.getColor(context, R.color.black)
            val white = ContextCompat.getColor(context, android.R.color.white)
            binding.root.setCardBackgroundColor(bg)
            binding.tvTituloItem.setTextColor(white)
            binding.tvDescripcionItem.setTextColor(white)
            binding.tvInfoExtraItem.setTextColor(white)
        } else {
            val bg = ContextCompat.getColor(context, android.R.color.white)
            val black = ContextCompat.getColor(context, android.R.color.black)
            binding.root.setCardBackgroundColor(bg)
            binding.tvTituloItem.setTextColor(black)
            binding.tvDescripcionItem.setTextColor(black)
            binding.tvInfoExtraItem.setTextColor(black)
        }
    }
}
