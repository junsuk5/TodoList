package dev.jsoh.myapplication.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jsoh.myapplication.models.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}