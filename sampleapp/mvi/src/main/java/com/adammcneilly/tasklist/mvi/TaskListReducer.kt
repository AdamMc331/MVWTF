package com.adammcneilly.tasklist.mvi

class TaskListReducer : Reducer<TaskListState>() {

    override fun reduce(action: BaseAction, state: TaskListState): TaskListState {
        return when (action) {
            is TaskListAction.TasksLoading -> TaskListState.Loading()
            is TaskListAction.TasksLoaded -> TaskListState.Loaded(action.tasks)
            is TaskListAction.TasksErrored -> TaskListState.Error()
            is TaskListAction.TaskAdded -> getStateWithNewTask(action.newTask, state)
            else -> state
        }
    }

    private fun getStateWithNewTask(
        newTask: Task,
        state: TaskListState
    ): TaskListState.Loaded {
        val currentTasks = (state as? TaskListState.Loaded)?.tasks.orEmpty()
        return TaskListState.Loaded(currentTasks + newTask)
    }
}