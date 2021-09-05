package br.com.dio.todolist.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @NonNull
    val title: String,
    @NonNull
    val hour: String,
    @NonNull
    val date: String,
)
