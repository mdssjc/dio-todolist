package br.com.dio.todolist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.com.dio.todolist.R
import br.com.dio.todolist.TaskApplication
import br.com.dio.todolist.data.Task
import br.com.dio.todolist.databinding.FragmentMainBinding
import br.com.dio.todolist.viewmodel.TaskViewModel
import br.com.dio.todolist.viewmodel.TaskViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as TaskApplication).database.taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentMainBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            mainFragment = this@MainFragment
            rvTasks.adapter = adapter
        }
        adapter.listenerEdit = { editTask(it) }
        adapter.listenerDelete = { deleteTask(it) }

        viewModel.listAll().observe(this.viewLifecycleOwner) {
            binding.includeEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            adapter.submitList(it)
        }
    }

    fun newTask() {
        findNavController().navigate(R.id.action_mainFragment_to_addTaskFragmentNew)
    }

    private fun editTask(task: Task) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToAddTaskFragmentEdit(
                taskId = task.id
            )
        )
    }

    private fun deleteTask(task: Task) {
        viewModel.delete(task)
    }
}
