package com.example.fluxcomposetodoapp.dispatcher

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.stores.Store.StoreChangeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Dispatcher
 * The Dispatcher receives the Action fired from the View and notifies the Store.
 * Viewから発火されたActionをDispatcherが受け取り、Storeに通知する
 **/
class Dispatcher {

    private val _actionFlow = MutableStateFlow<Action?>(null)
    val actionFlow: StateFlow<Action?> = _actionFlow.asStateFlow()

    private val _storeChangeFlow = MutableStateFlow<StoreChangeEvent?>(null)
    val storeChangeFlow: StateFlow<StoreChangeEvent?> = _storeChangeFlow.asStateFlow()

    companion object {
        private var instance: Dispatcher? = null

        fun get(): Dispatcher {
            return instance ?: synchronized(this) {
                instance ?: Dispatcher().also { instance = it }
            }
        }
    }

    fun dispatch(action: Action) {
        post(action)
    }

    fun emitChange(changeEvent: StoreChangeEvent) {
        _storeChangeFlow.value = changeEvent
    }

    private fun post(action: Action) {
        _actionFlow.value = action
    }
}