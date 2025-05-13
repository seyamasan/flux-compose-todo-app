package com.example.fluxcomposetodoapp.stores

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher

abstract class Store(
    private val dispatcher: Dispatcher
) {
    // Protected can only be accessed from subclasses
    // protectedは、サブクラスからのみアクセスできる
    protected fun emitStoreChange() {
        dispatcher.emitChange(changeEvent())
    }

    protected abstract fun changeEvent(): StoreChangeEvent

    abstract fun onAction(action: Action)

    interface StoreChangeEvent
}