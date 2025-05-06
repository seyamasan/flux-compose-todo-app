package com.example.fluxcomposetodoapp.stores

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActionKeys
import com.example.fluxcomposetodoapp.actions.TodoActionType
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoStoreTest {

    companion object {
        private lateinit var dispatcher: Dispatcher
        private lateinit var todoStore: TodoStore
        private val testDispatcher = UnconfinedTestDispatcher()

        @JvmStatic
        @BeforeClass
        fun setup() {
            Dispatchers.setMain(testDispatcher) // Replace Main Dispatcher with Dispatcher for Test.(Main DispatcherをTest用のDispatcherに置き換える)
            dispatcher = mockk(relaxed = true)
            todoStore = TodoStore(dispatcher)
        }
    }

    @After
    fun tearDown() {
        // Must be used in @After tearDown().(@After tearDown()で使用する必要がある)
        Dispatchers.resetMain() // Reset to the original Main Dispatcher.(元のMain Dispatcherにリセットする)
    }

    @Test
    fun should_create_todo_when_receive_create_action() = runTest {
        // Create Action
        val fakeTodoText = "New Todo"
        val fakeTodoData = Pair(fakeTodoText, false)
        val fakeActionBuilder = Action.type(TodoActionType.TODO_CREATE)
        fakeActionBuilder.setData(TodoActionKeys.KEY_TEXT, fakeTodoData)

        todoStore.onAction(fakeActionBuilder.build())

        val todos = todoStore.getTodos()
        assertThat(todos).hasSize(1)
        assertThat(todos[0].text).isEqualTo(fakeTodoText)
        assertThat(todos[0].complete).isFalse()
    }
}