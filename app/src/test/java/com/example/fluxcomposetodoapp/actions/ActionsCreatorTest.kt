package com.example.fluxcomposetodoapp.actions

import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test

class ActionsCreatorTest {

    companion object {
        private lateinit var dispatcher: Dispatcher
        private lateinit var actionsCreator: ActionsCreator

        // The method with @BeforeClass annotation is executed before the first test execution.
        // In the case of @Before annotation, it is executed for each function, while @BeforeClass annotation is executed once for each class.
        // 初回のテスト実行前に @BeforeClass アノテーションがついたメソッドが実行されます。
        // @Before アノテーションの場合、関数ごとに実行されるが、@BeforeClass アノテーションはクラスごとに一回実行される。
        @JvmStatic
        @BeforeClass
        fun setup() {
            // Mock the Dispatcher.(DispatcherをMockする)
            dispatcher = mockk(relaxed = true) // Returns simple values for all RELAXED functions.(relaxedすべての関数に対して単純な値を返す)
            // Create an ActionsCreator instance for testing.(テスト用のActionsCreatorインスタンスを作成)
            actionsCreator = ActionsCreator.get(dispatcher)
        }
    }

    @Test
    fun create_should_dispatch_create_action() = runTest {
        val todoText = "New Todo"
        val todoData = Pair(todoText, false)

        actionsCreator.create(todoData)

        // Start a coroutine validation block.(コルーチンの検証ブロックを開始)
        coVerify {
            // Confirm that the dispatch function has been called.(dispatch関数が呼び出されたことを確認)
            // Check that the contents of the Action passed to dispatch are correct.(dispatchに渡されたActionの中身が合っているか確認)
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_CREATE)
                    assertThat(action.data[TodoActionKeys.KEY_TEXT]).isEqualTo(todoData)
                    true
                }
            )
        }
    }

    @Test
    fun destroy_should_dispatch_destroy_action() = runTest {
        val todoId = 1L

        actionsCreator.destroy(todoId)

        coVerify {
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_DESTROY)
                    assertThat(action.data[TodoActionKeys.KEY_ID]).isEqualTo(todoId)
                    true
                }
            )
        }
    }

    @Test
    fun undo_destroy_should_dispatch_undo_destroy_action() = runTest {
        actionsCreator.undoDestroy()

        coVerify {
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_UNDO_DESTROY)
                    assertThat(action.data.size).isEqualTo(0)
                    true
                }
            )
        }
    }

    @Test
    fun toggle_complete_should_dispatch_correct_action() = runTest {
        val incompleteTodo = Todo(id = 1L, text = "Incomplete Todo", complete = false)

        actionsCreator.toggleComplete(incompleteTodo)

        coVerify {
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_COMPLETE)
                    assertThat(action.data[TodoActionKeys.KEY_ID]).isEqualTo(incompleteTodo.id)
                    true
                }
            )
        }
    }

    @Test
    fun toggle_complete_all_should_dispatch_toggle_complete_all_action() = runTest {
        actionsCreator.toggleCompleteAll()

        coVerify {
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_TOGGLE_COMPLETE_ALL)
                    assertThat(action.data.size).isEqualTo(0)
                    true
                }
            )
        }
    }

    @Test
    fun destroy_completed_should_dispatch_destroy_completed_action() = runTest {
        actionsCreator.destroyCompleted()

        coVerify {
            dispatcher.dispatch(
                match { action ->
                    assertThat(action.type).isEqualTo(TodoActionType.TODO_DESTROY_COMPLETED)
                    assertThat(action.data.size).isEqualTo(0)
                    true
                }
            )
        }
    }
}