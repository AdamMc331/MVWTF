package com.adammcneilly.tasklist.mvi

sealed class TaskListAction : BaseAction {
    object TasksLoading : TaskListAction()
    class TasksLoaded(val tasks: List<Task>) : TaskListAction()
    class TasksErrored(val error: Throwable) : TaskListAction()
    class TaskAdded(val newTask: Task) : TaskListAction()
}