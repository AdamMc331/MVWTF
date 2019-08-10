package com.adammcneilly.tasklist.mvi

import com.adammcneilly.tasklist.mvi.Task

interface TaskRepository {
    fun getTasks(): List<Task>
}