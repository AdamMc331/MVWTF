package com.adammcneilly.tasklist.mvp

/**
 * This is an implementation of our [TaskListContract.Model] which is the component responsible
 * for being a data source. In many real world applications, this class would fetch from a database
 * or remote server. For this example, we'll stub in a default list.
 */
class InMemoryTaskService : TaskListContract.Model {
    override fun getTasks(): List<Task> {
        return listOf(
            Task("Sample task 1"),
            Task("Sample task 2"),
            Task("Sample task 3"),
            Task("Sample task 4"),
            Task("Sample task 5"),
            Task("Sample task 6"),
            Task("Sample task 7"),
            Task("Sample task 8"),
            Task("Sample task 9"),
            Task("Sample task 10")
        )
    }
}