package com.dam.agendadam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dam.agendadam.dao.TareaDao
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.entity.TareaEntity
import com.dam.agendadam.entity.Usuario

// Anotación que define esta clase como una base de datos Room.
// Se indican las entidades que forman parte de la BD, la versión y si se exporta el esquema.
@Database(
    entities = [Usuario::class, TareaEntity::class],
    version = 1,
    exportSchema = false
)
// Clase abstracta que extiende RoomDatabase y representa la base de datos de la app.
abstract class AgendaDatabase : RoomDatabase() {

    // Proporciona el DAO para acceder a las operaciones sobre la tabla "usuarios".
    abstract fun usuarioDao(): UsuarioDao

    // Proporciona el DAO para acceder a las operaciones sobre la tabla "tareas".
    abstract fun tareaDao(): TareaDao
}
