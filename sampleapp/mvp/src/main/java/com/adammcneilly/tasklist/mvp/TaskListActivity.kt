package com.adammcneilly.tasklist.mvp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * An implementation of our [TaskListContract.View] which is responsible for all UI functions related
 * to displaying a list of tasks.
 *
 * NOTE: It's not perfect to have this view create the repository that is passed into the [presenter],
 * but since the view is the one responsible for creating the presenter, it was the easiest option
 * for now. If you use a DI framework such as dagger, you wouldn't need to worry about that.
 */
class TaskListActivity : AppCompatActivity(), TaskListContract.View {
    private val taskAdapter = TaskAdapter()
    private val presenter = TaskListPresenter(this, InMemoryTaskService())
    private var fab: FloatingActionButton? = null
    private var taskList: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        fab = findViewById(R.id.fab)
        taskList = findViewById(R.id.task_list)
        progressBar = findViewById(R.id.progress_bar)
        errorText = findViewById(R.id.error_text)
        setSupportActionBar(findViewById(R.id.toolbar))

        initializeRecyclerView()
        initializeFAB()

        presenter.restoreState(savedInstanceState)
    }

    override fun onDestroy() {
        presenter.viewDestroyed()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(presenter.getState())
        super.onSaveInstanceState(outState)
    }

    private fun initializeFAB() {
        fab?.setOnClickListener {
            presenter.addButtonClicked()
        }
    }

    private fun initializeRecyclerView() {
        taskList?.adapter = taskAdapter
        taskList?.layoutManager = LinearLayoutManager(this)
    }

    override fun showTasks(tasks: List<Task>) {
        taskList?.visibility = View.VISIBLE
        taskAdapter.tasks = tasks
    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun showError() {
        errorText?.visibility = View.VISIBLE
    }

    override fun hideTasks() {
        taskList?.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun hideError() {
        errorText?.visibility = View.GONE
    }
}
