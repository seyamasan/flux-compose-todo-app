package com.example.fluxcomposetodoapp.stores

import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActions
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Store
 * Receive notification from the Dispatcher, process it, and then notify the View.
 * Dispatcherから通知を受け取り、処理をした後にViewに通知する
 **/
class TodoStore(dispatcher: Dispatcher) : Store(dispatcher) {

    private val todos = mutableListOf<Todo>()
    private var lastDeleted: Todo? = null
    private val _storeChangeFlow = MutableSharedFlow<StoreChangeEvent>()
    val storeChangeFlow: SharedFlow<StoreChangeEvent> = _storeChangeFlow

    companion object {
        private var instance: TodoStore? = null

        fun get(dispatcher: Dispatcher): TodoStore {
            if (instance == null) {
                instance = TodoStore(dispatcher)
            }
            return instance!!
        }
    }

    init {
        // Subscribe to Dispatcher actionFlow
        // DispatcherのactionFlowを購読
        CoroutineScope(Dispatchers.Main).launch {
            // collect is a suspend function and must be executed in a coroutine.
            // collect は suspend 関数であるため、コルーチン内で実行する必要があります。
            dispatcher.actionFlow.collect { action ->
                onAction(action)
            }
        }

        // Subscribe to Dispatcher storeChangeFlow
        // DispatcherのstoreChangeFlowを購読
        CoroutineScope(Dispatchers.Main).launch {
            dispatcher.storeChangeFlow.collect { storeChangeEvent ->
                _storeChangeFlow.emit(storeChangeEvent)
            }
        }
    }

    fun getTodos(): List<Todo> = todos

    fun canUndo(): Boolean = lastDeleted != null

    override suspend fun onAction(action: Action) {
        when (action.type) {
            TodoActions.TODO_CREATE -> {
                val text = action.data[TodoActions.KEY_TEXT] as String
                create(text)
                emitStoreChange()
            }
            TodoActions.TODO_DESTROY -> {
                val id = action.data[TodoActions.KEY_ID] as Long
                destroy(id)
                emitStoreChange()
            }
            TodoActions.TODO_UNDO_DESTROY -> {
                undoDestroy()
                emitStoreChange()
            }
            TodoActions.TODO_COMPLETE -> {
                val id = action.data[TodoActions.KEY_ID] as Long
                updateComplete(id, true)
                emitStoreChange()
            }
            TodoActions.TODO_UNDO_COMPLETE -> {
                val id = action.data[TodoActions.KEY_ID] as Long
                updateComplete(id, false)
                emitStoreChange()
            }
            TodoActions.TODO_DESTROY_COMPLETED -> {
                destroyCompleted()
                emitStoreChange()
            }
            TodoActions.TODO_TOGGLE_COMPLETE_ALL -> {
                updateCompleteAll()
                emitStoreChange()
            }
        }
    }

    private fun destroyCompleted() {
        todos.removeIf { it.complete }
    }

    private fun updateCompleteAll() {
        val allComplete = areAllComplete()
        updateAllComplete(!allComplete)
    }

    private fun areAllComplete(): Boolean = todos.all { it.complete }

    private fun updateAllComplete(complete: Boolean) {
        todos.forEach { it.complete = complete }
    }

    private fun updateComplete(id: Long, complete: Boolean) {
        val todo = getById(id)
        todo?.complete = complete
    }

    private fun undoDestroy() {
        lastDeleted?.let {
            addElement(it.copy())
            lastDeleted = null
        }
    }

    private fun create(text: String) {
        val id = System.currentTimeMillis()
        val todo = Todo(id, text)
        addElement(todo)
    }

    private fun destroy(id: Long) {
        todos.find { it.id == id }?.let { todo ->
            lastDeleted = todo.copy()
            todos.remove(todo)
        }
    }

    private fun getById(id: Long): Todo? {
        return todos.find { it.id == id }
    }

    private fun addElement(todo: Todo) {
        todos.add(todo)
        todos.sort()
    }

    override fun changeEvent(): StoreChangeEvent {
        return TodoStoreChangeEvent()
    }

    class TodoStoreChangeEvent : StoreChangeEvent
}