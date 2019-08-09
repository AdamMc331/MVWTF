package com.adammcneilly.tasklist.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adammcneilly.tasklist.mvvm.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private val taskAdapter = TaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        binding.taskList.adapter = taskAdapter
        binding.taskList.layoutManager = LinearLayoutManager(this)
    }
}
