package com.example.fluxcomposetodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fluxcomposetodoapp.actions.ActionsCreator
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import com.example.fluxcomposetodoapp.stores.TodoStore
import com.example.fluxcomposetodoapp.ui.MainScreen
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View
 * Subscribe to the Store and update the View according to its status.
 * Storeを購読して、その状態に応じてViewを更新する
 **/
class MainActivity : ComponentActivity() {
    private lateinit var dispatcher: Dispatcher
    private lateinit var actionsCreator: ActionsCreator
    private lateinit var todoStore: TodoStore

    // CoroutineScope for executing coroutines in UI threads.
    // UIスレッドでコルーチンを実行するためのCoroutineScope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencies()

        enableEdgeToEdge()
        setContent {
            FluxComposeTodoAppTheme {
                MainScreen(
                    onAddClick = { addTodo(it) },
                    onDestroyClick = { destroyTodo(it) },
                    onUndoDestroyClick = { undoDestroy() },
                    onCheckedChange = { toggleComplete(it) },
                    onMainCheckedChange = { toggleCompleteAll() },
                    onClearCompletedClick = { clearCompleted() },
                    todoStore = todoStore
                )
            }
        }
    }

    // DI
    // 依存関係を注入
    // いずれはDIライブラリ使いたい
    private fun initDependencies() {
        dispatcher = Dispatcher.get()
        actionsCreator = ActionsCreator.get(dispatcher)
        todoStore = TodoStore.get(dispatcher)
    }

    private fun addTodo(data: Pair<String, Boolean>) {
        coroutineScope.launch {
            actionsCreator.create(data = data)
        }
    }

    private fun destroyTodo(id: Long) {
        coroutineScope.launch {
            actionsCreator.destroy(id = id)
        }
    }

    private fun undoDestroy() {
        coroutineScope.launch {
            actionsCreator.undoDestroy()
        }
    }

    private fun toggleComplete(todo: Todo) {
        coroutineScope.launch {
            actionsCreator.toggleComplete(todo)
        }
    }

    private fun toggleCompleteAll() {
        coroutineScope.launch {
            actionsCreator.toggleCompleteAll()
        }
    }

    private fun clearCompleted() {
        coroutineScope.launch {
            actionsCreator.destroyCompleted()
        }
    }
}