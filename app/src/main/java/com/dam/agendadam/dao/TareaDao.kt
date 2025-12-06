package com.dam.agendadam.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dam.agendadam.entity.TareaEntity
import kotlinx.coroutines.flow.Flow

// Interfaz DAO de Room para acceder a la tabla "tareas".
@Dao
interface TareaDao {

    // Devuelve todas las tareas en un Flow, ordenadas por id descendente (las más recientes primero).
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<TareaEntity>>

    // Devuelve un Flow con las tareas cuyo título contenga el texto indicado, también ordenadas por id descendente.
    @Query("SELECT * FROM tareas WHERE titulo LIKE '%' || :texto || '%' ORDER BY id DESC")
    fun buscarPorTitulo(texto: String): Flow<List<TareaEntity>>

    // Devuelve, de forma suspendida, la lista de tareas de un usuario concreto (filtrando por nombreUsuario).
    @Query("SELECT * FROM tareas WHERE nombreUsuario = :nombreUsuario ORDER BY id DESC")
    suspend fun obtenerTareasDeUsuario(nombreUsuario: String): List<TareaEntity>

    // Busca tareas de un usuario concreto cuyo título contenga un texto, todo en una sola consulta.
    @Query("SELECT * FROM tareas WHERE nombreUsuario = :nombreUsuario AND titulo LIKE '%' || :texto || '%' ORDER BY id DESC")
    suspend fun buscarPorTituloDeUsuario(nombreUsuario: String, texto: String): List<TareaEntity>

    // Inserta una nueva tarea en la base de datos.
    @Insert
    suspend fun insertar(tarea: TareaEntity)

    // Actualiza una tarea existente (mismo id) en la base de datos.
    @Update
    suspend fun actualizar(tarea: TareaEntity)

    // Elimina una tarea concreta de la base de datos.
    @Delete
    suspend fun borrar(tarea: TareaEntity)
}
