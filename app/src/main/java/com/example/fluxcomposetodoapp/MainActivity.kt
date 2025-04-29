package com.example.fluxcomposetodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.fluxcomposetodoapp.actions.ActionsCreator
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import com.example.fluxcomposetodoapp.stores.TodoStore
import com.example.fluxcomposetodoapp.ui.MainScreen
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var dispatcher: Dispatcher
    private lateinit var actionsCreator: ActionsCreator
    private lateinit var todoStore: TodoStore

    // CoroutineScope for executing coroutines in UI threads.
    // UIスレッドでコルーチンを実行するためのCoroutineScope
    private val addTodoScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencies()

        enableEdgeToEdge()
        setContent {
            FluxComposeTodoAppTheme {
                var inputText by rememberSaveable { mutableStateOf("") }
                var itemList by rememberSaveable { mutableStateOf<List<Todo>>(listOf()) }

                // Subscribe to TodoStore change events.
                // TodoStoreの変更イベントを購読
                LaunchedEffect(Unit) {
                    todoStore.storeChangeFlow.collect { _ ->
                        // Substituting a copy with toList() will recompose it.
                        // toList()でコピーしたものを代入すると再コンポーズできる
                        itemList = todoStore.getTodos().toList()
                    }
                }

                MainScreen(
                    inputText = inputText,
                    onInputChange = { newText ->
                        inputText = newText
                    },
                    isChecked = false,
                    onCheckedChange = {},
                    onAddClick = {
                        addTodo(inputText)
                    },
                    onClearCompletedClick = {},
                    itemList = itemList
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

    private fun addTodo(inputText: String) {
        if (validateInput(inputText)) {
            addTodoScope.launch {
                actionsCreator.create(inputText)
            }
        }
    }

    private fun validateInput(inputText: String): Boolean {
        return inputText.isNotEmpty()
    }
}