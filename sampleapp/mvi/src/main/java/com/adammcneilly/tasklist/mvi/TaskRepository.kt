package com.adammcneilly.tasklist.mvi

interface TaskRepository {
    fun getTasks(): List<Task>
}