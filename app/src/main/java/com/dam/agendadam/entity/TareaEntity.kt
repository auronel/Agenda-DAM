package com.dam.agendadam.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val prioridad: String,
    val urgente: Int,
    val completada: Boolean,
    val nombreUsuario: String
)