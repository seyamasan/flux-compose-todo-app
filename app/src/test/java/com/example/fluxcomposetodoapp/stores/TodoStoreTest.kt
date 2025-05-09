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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

    @Test
    fun should_create_todo_when_receive_create_action() = runTest {
        resetTodoList()

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

    @Test
    fun destroy_should_remove_todo_and_save_to_lastDeleted() = runTest {
        resetTodoList()

        val fakeTodoText = "Destroy Todo"
        val fakeTodoData = Pair(fakeTodoText, false)
        val fakeActionBuilder = Action.type(TodoActionType.TODO_CREATE)
        fakeActionBuilder.setData(TodoActionKeys.KEY_TEXT, fakeTodoData)

        todoStore.onAction(fakeActionBuilder.build())

        val createdTodoId = todoStore.getTodos().first().id

        val fakeActionBuilder2 = Action.type(TodoActionType.TODO_DESTROY)
        fakeActionBuilder2.setData(TodoActionKeys.KEY_ID, createdTodoId)
        todoStore.onAction(fakeActionBuilder2.build())

        assertThat(todoStore.getTodos()).isEmpty()
        assertThat(todoStore.canUndo()).isTrue()
    }

    @Test
    fun undo_destroy_should_restore_last_deleted_todo() = runTest {
        resetTodoList()

        // Create
        val fakeTodoText = "Restore Todo"
        val fakeTodoData = Pair(fakeTodoText, false)
        val fakeActionBuilder = Action.type(TodoActionType.TODO_CREATE)
        fakeActionBuilder.setData(TodoActionKeys.KEY_TEXT, fakeTodoData)

        todoStore.onAction(fakeActionBuilder.build())

        val createdTodo = todoStore.getTodos().first()

        // Destroy
        val fakeDestroyActionBuilder = Action.type(TodoActionType.TODO_DESTROY)
        fakeDestroyActionBuilder.setData(TodoActionKeys.KEY_ID, createdTodo.id)
        todoStore.onAction(fakeDestroyActionBuilder.build())

        assertThat(todoStore.getTodos()).isEmpty()
        assertThat(todoStore.canUndo()).isTrue()

        // Undo Destroy
        val undoAction = Action.type(TodoActionType.TODO_UNDO_DESTROY).build()
        todoStore.onAction(undoAction)

        val restoredTodos = todoStore.getTodos()
        assertThat(restoredTodos).hasSize(1)

        val restoredTodo = restoredTodos.first()
        assertThat(restoredTodo.text).isEqualTo(fakeTodoText)
        assertThat(restoredTodo.id).isEqualTo(createdTodo.id)
        assertThat(todoStore.canUndo()).isFalse()
    }

    @Test
    fun update_complete_should_change_todo_completion_status() = runTest {
        resetTodoList()

        // Create
        val fakeTodoText = "Change completion status todo"
        val fakeTodoData = Pair(fakeTodoText, false)
        val fakeActionBuilder = Action.type(TodoActionType.TODO_CREATE)
        fakeActionBuilder.setData(TodoActionKeys.KEY_TEXT, fakeTodoData)

        todoStore.onAction(fakeActionBuilder.build())
        val createdTodo = todoStore.getTodos().first()
        assertThat(createdTodo.complete).isFalse()

        // Complete
        val fakeCompleteActionBuilder = Action.type(TodoActionType.TODO_COMPLETE)
        fakeCompleteActionBuilder.setData(TodoActionKeys.KEY_ID, createdTodo.id)
        todoStore.onAction(fakeCompleteActionBuilder.build())

        val updatedTodo = todoStore.getTodos().first()
        assertThat(updatedTodo.complete).isTrue()

        // Undo Complete
        val fakeUndoCompleteActionBuilder = Action.type(TodoActionType.TODO_UNDO_COMPLETE)
        fakeUndoCompleteActionBuilder.setData(TodoActionKeys.KEY_ID, createdTodo.id)
        todoStore.onAction(fakeUndoCompleteActionBuilder.build())

        val revertedTodo = todoStore.getTodos().first()
        assertThat(revertedTodo.complete).isFalse()
    }

    private fun resetTodoList() {
        todoStore.setTodo(mutableListOf())
    }
}