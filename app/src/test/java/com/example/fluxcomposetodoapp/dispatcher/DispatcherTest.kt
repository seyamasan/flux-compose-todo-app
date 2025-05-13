package com.example.fluxcomposetodoapp.dispatcher

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActionType
import com.example.fluxcomposetodoapp.stores.Store
import com.example.fluxcomposetodoapp.stores.TodoStore
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.BeforeClass

/**
 * Referenced URL(参考にしたURL)
 * https://developer.android.com/kotlin/flow/test?hl=ja
**/
class DispatcherTest {
    companion object {
        private lateinit var dispatcher: Dispatcher
        private lateinit var fakeAction: Action

        @JvmStatic
        @BeforeClass
        fun setup() {
            dispatcher = Dispatcher.get()
            fakeAction = Action.type(TodoActionType.TODO_CREATE).build()
        }
    }

    @Test
    fun dispatch_should_emit_action_to_action_flow() {
        dispatcher.dispatch(fakeAction)

        val receivedAction: Action? = dispatcher.actionFlow.value

        assertThat(receivedAction).isEqualTo(fakeAction)
    }

    @Test
    fun emitChange_should_emit_event_to_storeChangeFlow() {
        val fakeEvent = TodoStore.TodoStoreChangeEvent()

        dispatcher.emitChange(fakeEvent)

        val receivedEvent: Store.StoreChangeEvent? = dispatcher.storeChangeFlow.value

        assertThat(receivedEvent).isEqualTo(fakeEvent)
    }
}