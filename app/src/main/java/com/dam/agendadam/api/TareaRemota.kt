package com.dam.agendadam.api

data class TareaRemota(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int
)

data class RespuestaTareasRemotas(
    val todos: List<TareaRemota>,
    val total: Int,
    val skip: Int,
    val limit: Int
)