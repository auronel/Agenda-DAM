package com.dam.agendadam.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.R
import com.dam.agendadam.databinding.ItemTareaBinding
import com.dam.agendadam.entity.TareaEntity

class TareaViewHolder(
    private val binding: ItemTareaBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tarea: TareaEntity, isSelected: Boolean) {
        val context = binding.root.context

        binding.tvTituloItem.text = tarea.titulo
        binding.tvDescripcionItem.text = tarea.descripcion

        val prioridadLabel = context.getString(R.string.tarea_item_prioridad)
        val urgenciaLabel = context.getString(R.string.tarea_item_urgencia)
        val pendiente = context.getString(R.string.tarea_estado_pendiente)
        val completada = context.getString(R.string.tarea_estado_completada)

        val estado = if (tarea.completada) completada else pendiente

        binding.tvInfoExtraItem.text =
            "$prioridadLabel ${tarea.prioridad} | $urgenciaLabel ${tarea.urgente} | $estado"

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
