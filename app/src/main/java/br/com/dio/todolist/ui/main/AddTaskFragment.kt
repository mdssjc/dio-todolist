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
import br.com.dio.todolist.databinding.FragmentAddTaskBinding
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import br.com.dio.todolist.viewmodel.TaskViewModel
import br.com.dio.todolist.viewmodel.TaskViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private var taskId: Int = 0
    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as TaskApplication).database.taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            addTaskFragment = this@AddTaskFragment
            toolbar.setNavigationOnClickListener { backToMain() }
            tilDate.editText?.setOnClickListener { editDate() }
            tilHour.editText?.setOnClickListener { editHour() }
        }
        arguments?.getInt(TASK_ID)?.let {
            viewModel.get(it).observe(this.viewLifecycleOwner) { task ->
                taskId = task.id
                binding.tilTitle.text = task.title
                binding.tilDate.text = task.date
                binding.tilHour.text = task.hour
            }
        }
    }

    fun backToMain() {
        findNavController().navigate(
            R.id.action_addTaskFragment_to_mainFragment
        )
    }

    fun saveTask() {
        val title = binding.tilTitle.text
        val date = binding.tilDate.text
        val hour = binding.tilHour.text

        if (taskId == 0) {
            viewModel.create(title, date, hour)
        } else {
            viewModel.update(taskId, title, date, hour)
        }
        backToMain()
    }

    private fun editHour() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val minute =
                if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
            val hour =
                if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

            binding.tilHour.text = "$hour:$minute"
        }
        timePicker.show(childFragmentManager, null)
    }

    private fun editDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()

        datePicker.addOnPositiveButtonClickListener {
            val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(Date().time) * -1
            binding.tilDate.text = Date(it + offset).format()
        }
        datePicker.show(childFragmentManager, "DATE_PICKER_TAG")
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
