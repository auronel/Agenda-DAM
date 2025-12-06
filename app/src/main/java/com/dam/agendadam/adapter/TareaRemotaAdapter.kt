package com.dam.agendadam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dam.agendadam.R
import com.dam.agendadam.api.TareaRemota
import com.dam.agendadam.databinding.ItemTareaRemotaBinding

// Adaptador para mostrar una lista de TareaRemota en un RecyclerView.
class TareaRemotaAdapter(
    // Lista de tareas remotas que se mostrarán.
    private var tareas: List<TareaRemota> = emptyList()
) : RecyclerView.Adapter<TareaRemotaAdapter.TareaRemotaViewHolder>() {

    // Actualiza la lista de tareas y notifica al RecyclerView para que se redibuje.
    fun actualizarLista(nuevaLista: List<TareaRemota>) {
        tareas = nuevaLista
        notifyDataSetChanged()
    }

    // Infla el layout de cada ítem remoto y crea un ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaRemotaViewHolder {
        val binding = ItemTareaRemotaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TareaRemotaViewHolder(binding)
    }

    // Enlaza la tarea remota de una posición concreta con el ViewHolder.
    override fun onBindViewHolder(holder: TareaRemotaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.bind(tarea)
    }

    // Devuelve cuántos elementos tiene la lista de tareas remotas.
    override fun getItemCount(): Int = tareas.size

    // ViewHolder encargado de pintar los datos de una TareaRemota en el layout.
    class TareaRemotaViewHolder(
        private val binding: ItemTareaRemotaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // Asigna los datos de la tarea remota a las vistas del ítem.
        fun bind(tarea: TareaRemota) {
            // Contexto para poder acceder a los recursos (strings).
            val ctx = binding.root.context

            // Muestra el título de la tarea que viene directamente de la API.
            binding.tvTituloTareaRemota.text = tarea.todo

            // Decide el texto del estado según si la tarea está completada o no,
            // usando recursos de strings para poder traducirlo.
            val estadoTexto = if (tarea.completed) {
                ctx.getString(R.string.api_estado_completada)
            } else {
                ctx.getString(R.string.api_estado_pendiente)
            }
            binding.tvEstadoTareaRemota.text = estadoTexto

            // Obtiene el prefijo "Usuario:" desde recursos y concatena el userId de la API.
            val usuarioPrefijo = ctx.getString(R.string.api_usuario_prefijo)
            binding.tvUsuarioTareaRemota.text = "$usuarioPrefijo ${tarea.userId}"
        }
    }
}
