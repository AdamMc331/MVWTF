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

    private val store: BaseStore<TaskListState> = BaseStore(
        TaskListState.Loading(),
        TaskListReducer()
    )

    init {
        store.subscribe(state::setValue)

        if (state.value == null) {
            fetchTasks()
        }
    }

    fun addButtonClicked() {
        val taskNumber = Random.nextInt(0, 100)
        val newTask = Task(description = "Random Task $taskNumber")
        store.dispatch(TaskListAction.TaskAdded(newTask))
    }

    private fun fetchTasks() {
        store.dispatch(TaskListAction.TasksLoading)

        try {
            val tasks = repository.getTasks()
            store.dispatch(TaskListAction.TasksLoaded(tasks))
        } catch (e: Throwable) {
            store.dispatch(TaskListAction.TasksErrored(e))
        }
    }
}