package com.dam.agendadam.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto singleton que se encarga de crear y exponer el cliente de la API de tareas.
// De esta forma, solo se crea una instancia de Retrofit para toda la app.
object TareaApiClient {

    // URL base del servicio REST que se va a consumir (en este caso DummyJSON).
    private const val BASE_URL = "https://dummyjson.com/"

    // Propiedad perezosa que crea el servicio de la API solo cuando se usa por primera vez.
    val service: TareaApiService by lazy {
        // Construye el objeto Retrofit con la URL base.
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Añade el convertidor Gson para transformar JSON en objetos Kotlin y viceversa.
            .addConverterFactory(GsonConverterFactory.create())
            // Crea la instancia de Retrofit.
            .build()
            // Genera la implementación de la interfaz TareaApiService a partir de las anotaciones.
            .create(TareaApiService::class.java)
    }
}
