package br.com.dio.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.dio.todolist.data.Task
import br.com.dio.todolist.data.TaskDao

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    fun get(id: Int) = taskDao.findById(id = id)
    fun listAll() = taskDao.findAll()
    suspend fun create(task: Task) = taskDao.insert(task = task)
    suspend fun update(task: Task) = taskDao.update(task = task)
    suspend fun delete(task: Task) = taskDao.delete(task = task)
}

class TaskViewModelFactory(private val taskDao: TaskDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
