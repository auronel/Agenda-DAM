package com.dam.agendadam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dam.agendadam.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): Usuario?
}
