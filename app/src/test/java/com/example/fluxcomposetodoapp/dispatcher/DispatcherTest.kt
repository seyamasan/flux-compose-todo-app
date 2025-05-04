package com.example.fluxcomposetodoapp.dispatcher

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActionType
import com.example.fluxcomposetodoapp.stores.Store
import com.example.fluxcomposetodoapp.stores.TodoStore
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dispatch_should_emit_action_to_action_flow() = runTest {
        var receivedAction: Action? = null

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            receivedAction = dispatcher.actionFlow.first()
        }

        dispatcher.dispatch(fakeAction)

        assertThat(receivedAction).isEqualTo(fakeAction)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitChange_should_emit_event_to_storeChangeFlow() = runTest {
        val fakeEvent = TodoStore.TodoStoreChangeEvent()
        var receivedEvent: Store.StoreChangeEvent? = null

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            receivedEvent = dispatcher.storeChangeFlow.first()
        }

        dispatcher.emitChange(fakeEvent)

        assertThat(receivedEvent).isEqualTo(fakeEvent)
    }
}