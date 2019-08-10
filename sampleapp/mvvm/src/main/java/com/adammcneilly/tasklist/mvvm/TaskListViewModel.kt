package com.adammcneilly.tasklist.mvvm

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
        setLoadedState(newTaskList)
    }

    private fun fetchTasks() {
        setLoadingState()

        try {
            val tasks = repository.getTasks()
            setLoadedState(tasks)
        } catch (e: Throwable) {
            setErrorState()
        }
    }

    private fun setErrorState() {
        state.value = TaskListState.Error()
    }

    private fun setLoadedState(tasks: List<Task>) {
        state.value = TaskListState.Loaded(tasks)
    }

    private fun setLoadingState() {
        state.value = TaskListState.Loading()
    }
}