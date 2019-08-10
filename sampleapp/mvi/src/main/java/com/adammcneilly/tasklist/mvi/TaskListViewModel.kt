package com.adammcneilly.tasklist.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class TaskListViewModel(private val repository: TaskRepository) : ViewModel() {
    private val state = MutableLiveData<TaskListState>()

    val tasks: LiveData<List<Task>> = Transformations.map(state) { state ->
        (state as? TaskListState.Loaded)?.tasks
    }

    val showLoading: LiveData<Boolean> = Transformations.map(state) { state ->
        state is TaskListState.Loading
    }

    val showTasks: LiveData<Boolean> = Transformations.map(state) { state ->
        state is TaskListState.Loaded
    }

    val showError: LiveData<Boolean> = Transformations.map(state) { state ->
        state is TaskListState.Error
    }

    init {
        if (state.value == null) {
            fetchTasks()
        }
    }

    fun addButtonClicked() {
        val taskNumber = Random.nextInt(0, 100)
        val newTask = Task(description = "Random Task $taskNumber")
        val newTaskList = (state.value as? TaskListState.Loaded)?.tasks.orEmpty() + newTask
        state.value = TaskListState.Loaded(newTaskList)
    }

    private fun fetchTasks() {
        state.value = TaskListState.Loading()

        try {
            val tasks = repository.getTasks()
            state.value = TaskListState.Loaded(tasks)
        } catch (e: Throwable) {
            state.value = TaskListState.Error()
        }
    }
}