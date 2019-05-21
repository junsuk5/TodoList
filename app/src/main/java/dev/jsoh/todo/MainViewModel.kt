package dev.jsoh.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import dev.jsoh.todo.models.Todo
import dev.jsoh.todo.repository.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var lastDeletedItem: Todo? = null

    private val db by lazy {
        Room.databaseBuilder(application, AppDatabase::class.java, "todo")
            .build()
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

    fun delete(todo: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            db.todoDao().delete(todo)
            lastDeletedItem = todo
        }
    }

    fun undo() {
        lastDeletedItem?.let {
            CoroutineScope(Dispatchers.IO).launch {
                db.todoDao().insert(it)
                lastDeletedItem = null
            }
        }
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            db.todoDao().deleteAll()
        }
    }

    fun swap(from: Todo, to: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            db.runInTransaction {
                val fromIndex = from.uid
                val toIndex = to.uid
                from.uid = toIndex
                to.uid = fromIndex
                update(from)
                update(to)
            }
        }
    }
}