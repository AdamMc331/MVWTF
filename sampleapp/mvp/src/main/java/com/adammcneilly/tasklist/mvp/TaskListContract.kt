package com.adammcneilly.tasklist.mvp

import android.os.Bundle

/**
 * In MVP, a contract class can define the required behavior of the [Model], [View], and [Presenter].
 */
class TaskListContract {

    /**
     * The view is responsible for any behavior that belongs to the UI. In this case that includes
     * displaying a list, and navigating to a new view.
     */
    interface View {
        fun showTasks(tasks: List<Task>)
        fun hideTasks()
        fun showLoading()
        fun hideLoading()
        fun showError()
        fun hideError()
    }

    /**
     * The presenter is responsible for UI logic that we want to test that shouldn't be in the view. Here,
     * that means fetching the tasks, and handling click events that come from the view.
     *
     * This also includes being notified when the view is created and destroyed.
     */
    interface Presenter {
        fun addButtonClicked()
        fun viewDestroyed()
        fun getState(): Bundle
        fun restoreState(bundle: Bundle?)
    }

    /**
     * The model is responsible for being the data source for the feature. This could include pulling
     * data from any remote source or database. The important part is understanding that the data source
     * is not directly tied to the presenter.
     */
    interface Model {
        fun getTasks(): List<Task>
    }
}