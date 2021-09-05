package br.com.dio.todolist.viewmodel

import androidx.lifecycle.*
import br.com.dio.todolist.data.Task
import br.com.dio.todolist.data.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    fun get(id: Int): LiveData<Task> {
        return taskDao.findById(id).asLiveData()
    }

    fun listAll(): LiveData<List<Task>> {
        return taskDao.findAll().asLiveData()
    }

    fun create(title: String, hour: String, date: String) {
        if (isValid(title, hour, date)) {
            val task = Task(0, title, hour, date)
            viewModelScope.launch {
                taskDao.insert(task)
            }
        }
    }

    fun update(id: Int, title: String, hour: String, date: String) {
        if (isValid(title, hour, date)) {
            val task = Task(id, title, hour, date)
            viewModelScope.launch {
                taskDao.update(task)
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    private fun isValid(title: String, hour: String, date: String): Boolean {
        return !(title.isBlank() || hour.isBlank() || date.isBlank())
    }
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
