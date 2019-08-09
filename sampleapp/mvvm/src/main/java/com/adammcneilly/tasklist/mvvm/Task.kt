package com.adammcneilly.tasklist.mvvm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val description: String
) : Parcelable