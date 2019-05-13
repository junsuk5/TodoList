package dev.jsoh.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import dev.jsoh.myapplication.models.Todo
import dev.jsoh.myapplication.repository.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy {
        Room.databaseBuilder(application, AppDatabase::class.java, "todo").build()
    }

    fun todos() = db.todoDao().getAll()

    fun insert(todo: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            db.todoDao().insert(todo)
        }
    }

    fun update(todo: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            db.todoDao().update(todo)
        }
    }
}