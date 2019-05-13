package dev.jsoh.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.jsoh.myapplication.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY isDone, uid")
    fun getAll(): LiveData<List<Todo>>

    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Update
    fun update(todo: Todo)
}