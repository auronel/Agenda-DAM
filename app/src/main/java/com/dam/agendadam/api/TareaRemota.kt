package com.dam.agendadam.api

// Modelo de datos que representa una tarea remota tal y como viene de la API.
data class TareaRemota(
    // Identificador único de la tarea.
    val id: Int,
    // Texto o título de la tarea.
    val todo: String,
    // Indica si la tarea está completada (true) o pendiente (false).
    val completed: Boolean,
    // Identificador del usuario al que pertenece la tarea.
    val userId: Int
)

// Modelo de la respuesta completa que devuelve el endpoint de "todos".
data class RespuestaTareasRemotas(
    // Lista de tareas remotas devueltas por la API.
    val todos: List<TareaRemota>,
    // Número total de tareas disponibles en el servidor.
    val total: Int,
    // Número de elementos que se han saltado (para paginación).
    val skip: Int,
    // Límite de elementos devueltos en esta petición.
    val limit: Int
)
