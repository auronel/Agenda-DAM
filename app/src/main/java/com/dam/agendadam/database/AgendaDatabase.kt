package com.dam.agendadam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dam.agendadam.dao.TareaDao
import com.dam.agendadam.dao.UsuarioDao
import com.dam.agendadam.entity.TareaEntity
import com.dam.agendadam.entity.Usuario

@Database(
    entities = [Usuario::class, TareaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun tareaDao(): TareaDao

}
