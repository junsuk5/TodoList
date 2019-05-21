package dev.jsoh.todo.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.jsoh.todo.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY isDone, uid")
    fun getAll(): LiveData<List<Todo>>

    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Query("DELETE FROM todo")
    fun deleteAll()

    @Update
    fun update(todo: Todo)
}