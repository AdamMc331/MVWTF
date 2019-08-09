package com.adammcneilly.tasklist.mvp

import android.os.Bundle
import kotlin.random.Random

/**
 * The presenter handles the UI logic for displaying a list of tasks.
 *
 * Notice that dependency injection is used to supply a [view] and [model]. This helps enforce a
 * separation of concerns between the components, but also allows for testability so that we can
 * mock these parameters in unit tests.
 */
class TaskListPresenter(
    private var view: TaskListContract.View?,
    private val model: TaskListContract.Model
) : TaskListContract.Presenter {

    private var state: TaskListState? = null
        set(value) {
            field = value

            when (value) {
                is TaskListState.Loading -> {
                    view?.showLoading()
                    view?.hideTasks()
                    view?.hideError()
                }
                is TaskListState.Loaded -> {
                    view?.hideLoading()
                    view?.showTasks(value.tasks)
                    view?.hideError()
                }
                is TaskListState.Error -> {
                    view?.hideLoading()
                    view?.hideTasks()
                    view?.showError()
                }
            }
        }

    override fun addButtonClicked() {
        val taskNumber = Random.nextInt(0, 100)
        val newTask = Task(description = "Random Task $taskNumber")
        val newTaskList = (state as? TaskListState.Loaded)?.tasks.orEmpty() + newTask
        state = TaskListState.Loaded(newTaskList)
    }

    override fun viewDestroyed() {
        view = null
    }

    override fun getState(): Bundle {
        return Bundle().apply {
            putParcelable(ARG_STATE, state)
        }
    }

    override fun restoreState(bundle: Bundle?) {
        if (bundle == null) {
            fetchTasks()
        } else {
            state = bundle.getParcelable(ARG_STATE)
        }
    }

    private fun fetchTasks() {
        state = TaskListState.Loading()

        try {
            val tasks = model.getTasks()
            state = TaskListState.Loaded(tasks)
        } catch (e: Throwable) {
            state = TaskListState.Error()
        }
    }

    companion object {
        const val ARG_STATE = "presenter_state"
    }
}