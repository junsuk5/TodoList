package dev.jsoh.todo.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jsoh.todo.models.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}