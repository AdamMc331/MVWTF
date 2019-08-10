package com.adammcneilly.tasklist.mvi

interface BaseAction

interface BaseState

abstract class Reducer<S : BaseState> {
    abstract fun reduce(action: BaseAction, state: S): S
}

class BaseStore<S : BaseState>(
    initialState: S,
    private val reducer: Reducer<S>
) {
    private var stateListener: ((S) -> Unit)? = null

    private var currentState: S = initialState
        set(value) {
            field = value
            stateListener?.invoke(value)
        }

    fun dispatch(action: BaseAction) {
        currentState = reducer.reduce(action, currentState)
    }

    fun subscribe(stateListener: ((S) -> Unit)?) {
        this.stateListener = stateListener
    }
}