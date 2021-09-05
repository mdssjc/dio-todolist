package br.com.dio.todolist

import android.app.Application
import br.com.dio.todolist.data.TaskDatabase

class TaskApplication : Application() {
    val database: TaskDatabase by lazy { TaskDatabase.getDatabase(this) }
}
