package com.adammcneilly.tasklist.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adammcneilly.tasklist.mvvm.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var viewModel: TaskListViewModel
    private val taskAdapter = TaskAdapter()

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = InMemoryTaskService()

            @Suppress("UNCHECKED_CAST")
            return TaskListViewModel(repository) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupViewModel()
        initializeRecyclerView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.tasks.observe(this, Observer { tasks ->
            tasks?.let(taskAdapter::tasks::set)
        })
    }

    private fun initializeRecyclerView() {
        binding.taskList.adapter = taskAdapter
        binding.taskList.layoutManager = LinearLayoutManager(this)
    }
}
