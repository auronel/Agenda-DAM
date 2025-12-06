package com.dam.agendadam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.R
import com.dam.agendadam.api.TareaRemota
import com.dam.agendadam.databinding.ItemTareaRemotaBinding

class TareaRemotaAdapter(
    private var tareas: List<TareaRemota> = emptyList()
) : RecyclerView.Adapter<TareaRemotaAdapter.TareaRemotaViewHolder>() {

    fun actualizarLista(nuevaLista: List<TareaRemota>) {
        tareas = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaRemotaViewHolder {
        val binding = ItemTareaRemotaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TareaRemotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareaRemotaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.bind(tarea)
    }

    override fun getItemCount(): Int = tareas.size

    class TareaRemotaViewHolder(
        private val binding: ItemTareaRemotaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tarea: TareaRemota) {
            val ctx = binding.root.context

            binding.tvTituloTareaRemota.text = tarea.todo

            val estadoTexto = if (tarea.completed) {
                ctx.getString(R.string.api_estado_completada)
            } else {
                ctx.getString(R.string.api_estado_pendiente)
            }
            binding.tvEstadoTareaRemota.text = estadoTexto

            val usuarioPrefijo = ctx.getString(R.string.api_usuario_prefijo)
            binding.tvUsuarioTareaRemota.text = "$usuarioPrefijo ${tarea.userId}"
        }
    }
}
