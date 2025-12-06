package com.dam.agendadam.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Marca esta clase como una entidad de Room asociada a la tabla "tareas".
@Entity(tableName = "tareas")
data class TareaEntity(
    // Clave primaria de la tabla, se autogenera al insertar una nueva tarea.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Título de la tarea que verá el usuario.
    val titulo: String,
    // Descripción o detalle de la tarea.
    val descripcion: String,
    // Prioridad de la tarea (por ejemplo, Alta/Media/Baja).
    val prioridad: String,
    // Nivel de urgencia numérico (por ejemplo, el valor del SeekBar).
    val urgente: Int,
    // Indica si la tarea está completada (true) o pendiente (false).
    val completada: Boolean,
    // Nombre del usuario al que pertenece esta tarea (sirve para filtrar por usuario).
    val nombreUsuario: String
)
