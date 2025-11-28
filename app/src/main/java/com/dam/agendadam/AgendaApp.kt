package com.dam.agendadam

import android.app.Application
import androidx.room.Room
import com.dam.agendadam.database.AgendaDatabase

class AgendaApp : Application() {
    @Volatile
    private var INSTANCE: AgendaDatabase? = null

    fun getDatabase(): AgendaDatabase {
        // Si la instancia ya existe, la devolvemos
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                applicationContext,
                AgendaDatabase::class.java,
                "agenda_database"
            ).build()
            INSTANCE = instance
            // Devolvemos la instancia
            instance
        }
    }
}
