package com.dam.agendadam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dam.agendadam.entity.Usuario

// DAO de Room para gestionar la tabla "usuarios".
@Dao
interface UsuarioDao {

    // Inserta un nuevo usuario en la base de datos.
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Intenta hacer login: busca un usuario cuyo email y password coincidan.
    // Devuelve un Usuario si lo encuentra o null si no existe.
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): Usuario?

    // Obtiene un usuario a partir de su email (por ejemplo, para comprobar si ya existe).
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerPorEmail(email: String): Usuario?

    // Obtiene un usuario a partir de su id (útil para recargar datos de perfil guardando solo el id).
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    // Actualiza los datos de un usuario existente (mismo id) en la base de datos.
    @Update
    suspend fun actualizar(usuario: Usuario)
}
