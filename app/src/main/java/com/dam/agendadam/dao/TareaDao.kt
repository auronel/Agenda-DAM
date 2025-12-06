package com.dam.agendadam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dam.agendadam.entity.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE titulo LIKE '%' || :texto || '%' ORDER BY id DESC")
    fun buscarPorTitulo(texto: String): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE nombreUsuario = :nombreUsuario ORDER BY id DESC")
    suspend fun obtenerTareasDeUsuario(nombreUsuario: String): List<TareaEntity>

    @Query("SELECT * FROM tareas WHERE nombreUsuario = :nombreUsuario AND titulo LIKE '%' || :texto || '%' ORDER BY id DESC")
    suspend fun buscarPorTituloDeUsuario(nombreUsuario: String, texto: String): List<TareaEntity>

    @Insert
    suspend fun insertar(tarea: TareaEntity)

    @Update
    suspend fun actualizar(tarea: TareaEntity)

    @Delete
    suspend fun borrar(tarea: TareaEntity)
}
