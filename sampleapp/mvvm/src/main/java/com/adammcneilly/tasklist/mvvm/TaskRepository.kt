package com.adammcneilly.tasklist.mvvm

interface TaskRepository {
    fun getTasks(): List<Task>
}