package com.adammcneilly.tasklist.mvvm

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityCondition")
fun visibleIf(view: View, condition: Boolean?) {
    view.visibility = if (condition == true) View.VISIBLE else View.GONE
}