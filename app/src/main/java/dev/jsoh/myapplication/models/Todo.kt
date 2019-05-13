package dev.jsoh.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    val text: String?,
    var isDone: Boolean = false
)