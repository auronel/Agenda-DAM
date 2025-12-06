package com.dam.agendadam.api

import retrofit2.http.GET

// Interfaz que define las llamadas HTTP a la API de tareas remotas.
interface TareaApiService {

    // Realiza una petición GET a la ruta "todos" de la API.
    // Al ser 'suspend', se puede llamar desde corrutinas sin bloquear el hilo principal.
    @GET("todos")
    suspend fun obtenerTareas(): RespuestaTareasRemotas
}
