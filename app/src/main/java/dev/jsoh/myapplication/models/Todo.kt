package dev.jsoh.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val text: String?,
    val isDone: Boolean = false
)