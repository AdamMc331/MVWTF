package com.adammcneilly.tasklist.mvvm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class TaskListState : Parcelable {
    @Parcelize class Loading : TaskListState()
    @Parcelize class Loaded(val tasks: List<Task>) : TaskListState()
    @Parcelize class Error : TaskListState()
}