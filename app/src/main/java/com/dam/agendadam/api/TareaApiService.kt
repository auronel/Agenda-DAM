package com.dam.agendadam.api

import retrofit2.http.GET

interface TareaApiService {

    @GET("todos")
    suspend fun obtenerTareas(): RespuestaTareasRemotas
}