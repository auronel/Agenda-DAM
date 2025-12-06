package com.dam.agendadam.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TareaApiClient {

    private const val BASE_URL = "https://dummyjson.com/"

    val service: TareaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TareaApiService::class.java)
    }
}