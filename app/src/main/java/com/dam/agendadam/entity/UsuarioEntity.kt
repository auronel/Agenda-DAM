package com.dam.agendadam.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Marca esta clase como una entidad de Room asociada a la tabla "usuarios".
@Entity(tableName = "usuarios")
data class Usuario(
    // Clave primaria autogenerada para identificar de forma única a cada usuario.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Nombre de usuario que se usará en la app (para mostrar y para relacionar tareas).
    val nombreUsuario: String,
    // Correo electrónico del usuario, también útil para el login o comprobaciones.
    val email: String,
    // Contraseña que se almacenará para validar el inicio de sesión.
    val password: String
)
