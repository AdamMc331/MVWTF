theme: Simple
slidenumbers: true
autoscale: true
footer: @AdamMc331<br/>#AndroidSummit
build-lists: true

## MVWTF: Demystifying Architecture Patterns

### Adam McNeilly - @AdamMc331

---

# You May Have Heard These Buzzwords:

* MVC
* MVP
* MVVM
* MVI
* MVU??

[.build-lists: false]

---

# Why Are There So Many?

---

# What's The Difference?

---

# Which One Should I Use?

---

# Which One Should I Use?

![inline](itdepends.png)

---

# Why Do We Need Architecture Patterns?

---

# More Buzzwords!

* Maintainability
* Extensibility
* Robust
* Testable

---

# Let's Start With One Simple Truth

---

# You Can't Put Everything In The Activity

---

# Why Not?

* Not readable
* Difficult to add new code
* Difficult to change existing code
* Can't write Junit tests for this

---

# We Need To Break Up Our Code

---

# Let's Explore Some Options

---

# Model-View-Controller

- One of the earliest architecture patterns
- Introduced in the 1970s as a way to organize code
- Divides application to three parts

---

# Model

- This is your data source
- Database, remote server, etc
- It does not care about the view

---

# View

- This is the visual representation of information
- Does not care where this data came from
- Only responsible for displaying data
- If your view has a conditional, consider refactoring

---

# Controller

- Handles user inputs
- Validates if necessary
- Passes input to model
- Passes model response to view

---

# The Model & View Components Are The Same For All Patterns

---

![inline](mvimprov.png) 

---

# Model-View-WhateverTheFYouWant

---

# Why Do We Have So Many Options For This Third Component? 

---

# Let's Break Them Down

---

# Model-View-Controller

![inline](mvcdiagram.png)

---

# Why Don't We Use This For Android?

---

# Why Don't We Use This For Android?

![inline](mvcandroiddiagram.png)

---

# Why Don't We Use This For Android?

- We can't write Junit tests for an Activity
 - We can't unit test our UI logic
- We don't really have a separation of concerns here

---

# Model-View-Presenter

---

# Model-View-Presenter

* Similar to the last pattern
* Moves our presentation logic out of the Activity class

---

# Model-View-Presenter

![inline](mvp.png)

---

# Why Is This Better?

- UI logic is outside of the Activity, and now supports Junit tests
- Our concerns are separated again

---

# MVP Implementation

---

# Contract Class

```kotlin
class TaskListContract {

    interface View {
        fun showTasks(tasks: List<Task>)
    }

    interface Presenter {
        fun viewCreated()
        fun viewDestroyed()
    }

    interface Model {
        fun getTasks(): List<Task>
    }
}
```

---

# Model

```kotlin
class InMemoryTaskService : TaskListContract.Model {
    override fun getTasks(): List<Task> {
        return listOf(
                Task("Sample task 1"),
                Task("Sample task 2")
        )
    }
}
```

---

# View

```kotlin
class TaskListActivity : AppCompatActivity(), TaskListContract.View {
    private val taskAdapter = TaskAdapter()
    private val presenter = TaskListPresenter(this, TaskRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        presenter.viewCreated()
    }

    override fun showTasks(tasks: List<Task>) {
        taskAdapter.tasks = tasks
    }

    override fun onDestroy() {
        presenter.viewDestroyed()
        super.onDestroy()
    }
}
```

---

# Presenter

```kotlin
class TaskListPresenter(
        private var view: TaskListContract.View?,
        private val model: TaskListContract.Model
) : TaskListContract.Presenter {

    override fun viewCreated() {
        val tasks = model.getTasks()
        view?.showTasks(tasks)
    }

    override fun viewDestroyed() {
        view = null
    }
}
```

---

# Is That Enough?

- View does nothing but display data
- Data fetching is all handled by model
- Presentation of data is handled by presenter
- Everything is separated, everything is testable
- If you think this is good enough, use it!

---

# What's Different About MVVM? 

---

# The Presenter Doesn't Need To Care About The View

---

# Model-View-ViewModel

![inline](mvvmdiagram.png)

---

# MVVM Implementation

---

# Model Doesn't Change (much)

```kotlin
interface TaskRepository {
    fun getTasks(): List<Task>
}

class InMemoryTaskService : TaskRepository {

    override fun getTasks(): List<Task> {
        return listOf(...)
    }
}
```

---

# ViewModel

```kotlin
class TaskListViewModel(
        private val repository: TaskRepository
) {
    private val tasks = MutableLiveData<List<Task>>()
    fun getTasks(): LiveData<List<Task>> = tasks

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        tasks.value = repository.getTasks()
    }
}
```

---

# View

```kotlin
class TaskListActivity : AppCompatActivity() {
    private val adapter = TaskAdapter()
    private val viewModel = TaskListviewModel(repository = InMemoryTaskService())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.getTasks().observe(this, Observer { tasks ->
            adapter.tasks = tasks
        })
    }
}
```

---

# This Is Pretty Close To MVP, With One New Benefit

---

# Since ViewModel Doesn't Reference View, We Can Leverage Android ViewModel To Outlast Config Changes

---

# Handle Rotation In MVP

1. Update your presenter to save/restore state
2. Modify the view to call appropriate save/restore methods

---

# Handle Rotation In MVP

```kotlin
class TaskListContract {
    interface Presenter {
        // New:
        fun getState(): Bundle
        fun restoreState(bundle: Bundle?)
    }
}
```

---

# Handle Rotation In MVP

```kotlin
class TaskListActivity : AppCompatActivity(), TaskListContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
        presenter.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(presenter.getState())
        super.onSaveInstanceState(outState)
    }
}
```

---

# Handle Rotation In MVVM

1. Have ViewModel class extend the Android ViewModel class
2. Update Activity to use ViewModelProviders
3. Since Android's ViewModel outlasts config changes, no need to save/restore state, just re-subscribe

---

# Handle Rotation In MVVM

```kotlin
class TaskListViewModel(
        private val repository: TaskRepository
) : ViewModel() {
    // ...
}
```

---

# Handle Rotation In MVVM

```kotlin
class TaskListActivity : AppCompatActivity() {
    private lateinit var viewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskListViewModel::class.java)

        viewModel.tasks.observe(this, Observer(taskAdapter::tasks::set))
    }
}
```

---

# Is That Enough?

- View does nothing but display data
- Data fetching is all handled by model
- ViewModel handles all UI logic
- We can easily save state across config changes
- Everything is separated, everything is testable
- If you think this is good enough, use it!


