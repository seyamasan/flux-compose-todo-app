package com.example.fluxcomposetodoapp.stores

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher

/**
 * About suspend
 * suspendについて
 * https://developer.android.com/kotlin/coroutines/coroutines-adv?hl=ja
 **/

abstract class Store(
    private val dispatcher: Dispatcher
) {
    // Protected can only be accessed from subclasses
    // protectedは、サブクラスからのみアクセスできる
    protected suspend fun emitStoreChange() {
        dispatcher.emitChange(changeEvent())
    }

    protected abstract fun changeEvent(): StoreChangeEvent

    // It is a suspend function, so calling it does not block the main thread.
    // suspend 関数なので、呼び出してもメインスレッドをブロックしない (suspend == 保留)
    abstract suspend fun onAction(action: Action)

    interface StoreChangeEvent
}