package com.dam.agendadam

import android.app.Application
import androidx.room.Room
import com.dam.agendadam.database.AgendaDatabase

// Clase Application que se usa para crear y mantener una única instancia de la base de datos.
class AgendaApp : Application() {
    // Referencia a la instancia de la base de datos. Volatile para que los hilos vean siempre el valor actualizado.
    @Volatile
    private var INSTANCE: AgendaDatabase? = null

    // Devuelve la base de datos de la app.
    fun getDatabase(): AgendaDatabase {
        // Si la instancia ya existe, la devolvemos.
        return INSTANCE ?: synchronized(this) {
            // Si era null, aquí se crea la base de datos Room con el nombre "agenda_database".
            val instance = Room.databaseBuilder(
                applicationContext,
                AgendaDatabase::class.java,
                "agenda_database"
            ).build()
            // Guardamos la instancia creada para reutilizarla.
            INSTANCE = instance
            // Devolvemos la instancia.
            instance
        }
    }
}
