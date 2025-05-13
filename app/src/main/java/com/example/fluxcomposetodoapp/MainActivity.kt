package com.example.fluxcomposetodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fluxcomposetodoapp.actions.ActionsCreator
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.stores.TodoStore
import com.example.fluxcomposetodoapp.ui.MainScreen
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme

/**
 * View
 * Subscribe to the Store and update the View according to its status.
 * Storeを購読して、その状態に応じてViewを更新する
 **/
class MainActivity : ComponentActivity() {
    private lateinit var dispatcher: Dispatcher
    private lateinit var actionsCreator: ActionsCreator
    private lateinit var todoStore: TodoStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencies()

        enableEdgeToEdge()
        setContent {
            FluxComposeTodoAppTheme {
                MainScreen(
                    onAddClick = { actionsCreator.create(data = it) },
                    onDestroyClick = { actionsCreator.destroy(id = it) },
                    onUndoDestroyClick = { actionsCreator.undoDestroy() },
                    onCheckedChange = { actionsCreator.toggleComplete(it) },
                    onMainCheckedChange = { actionsCreator.toggleCompleteAll() },
                    onClearCompletedClick = { actionsCreator.destroyCompleted() },
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
}