package com.example.fluxcomposetodoapp.dispatcher

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActionType
import com.example.fluxcomposetodoapp.stores.Store
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Dispatcher
 * The Dispatcher receives the Action fired from the View and notifies the Store.
 * Viewから発火されたActionをDispatcherが受け取り、Storeに通知する
 **/
class Dispatcher {

    private val _actionFlow = MutableSharedFlow<Action>()
    val actionFlow: SharedFlow<Action> = _actionFlow

    private val _storeChangeFlow = MutableSharedFlow<Store.StoreChangeEvent>()
    val storeChangeFlow: SharedFlow<Store.StoreChangeEvent> = _storeChangeFlow

    companion object {
        private var instance: Dispatcher? = null

        fun get(): Dispatcher {
            return instance ?: synchronized(this) {
                instance ?: Dispatcher().also { instance = it }
            }
        }
    }

    suspend fun dispatch(action: Action) {
        post(action)
    }

    suspend fun emitChange(changeEvent: Store.StoreChangeEvent) {
        _storeChangeFlow.emit(changeEvent)
    }

    private suspend fun post(action: Action) {
        _actionFlow.emit(action)
    }
}