package com.example.fluxcomposetodoapp.dispatcher

import com.example.fluxcomposetodoapp.actions.Action
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

    suspend fun dispatch(type: String, vararg data: Any) {
        if (data.size % 2 != 0) {
            throw IllegalArgumentException(
                "Data must be a valid list of key,value pairs.\n" +
                "（データは key と value のペアのリストである必要があります）"
            )
        }

        val actionBuilder = Action.type(type)
        var i = 0
        while (i < data.size) {
            val key = data[i++] as String
            val value = data[i++]
            actionBuilder.bundle(key, value)
        }
        val action = actionBuilder.build()
        post(action)
    }

    suspend fun emitChange(changeEvent: Store.StoreChangeEvent) {
        _storeChangeFlow.emit(changeEvent)
    }

    private suspend fun post(action: Action) {
        _actionFlow.emit(action)
    }
}