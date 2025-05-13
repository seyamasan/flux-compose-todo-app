package com.example.fluxcomposetodoapp.stores

import androidx.annotation.VisibleForTesting
import com.example.fluxcomposetodoapp.actions.Action
import com.example.fluxcomposetodoapp.actions.TodoActionKeys
import com.example.fluxcomposetodoapp.actions.TodoActionType
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Store
 * Receive notification from the Dispatcher, process it, and then notify the View.
 * Dispatcherから通知を受け取り、処理をした後にViewに通知する
 **/
class TodoStore(dispatcher: Dispatcher) : Store(dispatcher) {

    private var todos = mutableListOf<Todo>()
    private var lastDeleted: Todo? = null

    private val _storeChangeFlow = MutableStateFlow<StoreChangeEvent?>(null)
    val storeChangeFlow: StateFlow<StoreChangeEvent?> = _storeChangeFlow.asStateFlow()

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
        dispatcher.actionFlow.onEach { action ->
            action?.let {
                onAction(it)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))

        // Subscribe to Dispatcher storeChangeFlow
        // DispatcherのstoreChangeFlowを購読
        dispatcher.storeChangeFlow.onEach { storeChangeEvent ->
            _storeChangeFlow.value = storeChangeEvent
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    fun getTodos(): List<Todo> = todos

    fun canUndo(): Boolean = lastDeleted != null

    fun resetLastDeleted() {
        lastDeleted = null
    }

    override fun onAction(action: Action) {
        when (action.type) {
            TodoActionType.TODO_CREATE -> {
                val data = action.data[TodoActionKeys.KEY_TEXT] as? Pair<*, *>
                    ?: return  // first(text: String), second(complete: Boolean)
                create(data)
                emitStoreChange()
            }
            TodoActionType.TODO_DESTROY -> {
                val id = action.data[TodoActionKeys.KEY_ID] as? Long
                    ?: return
                destroy(id)
                emitStoreChange()
            }
            TodoActionType.TODO_UNDO_DESTROY -> {
                undoDestroy()
                emitStoreChange()
            }
            TodoActionType.TODO_COMPLETE -> {
                val id = action.data[TodoActionKeys.KEY_ID] as? Long
                    ?: return
                updateComplete(id, true)
                emitStoreChange()
            }
            TodoActionType.TODO_UNDO_COMPLETE -> {
                val id = action.data[TodoActionKeys.KEY_ID] as? Long
                    ?: return
                updateComplete(id, false)
                emitStoreChange()
            }
            TodoActionType.TODO_DESTROY_COMPLETED -> {
                destroyCompleted()
                emitStoreChange()
            }
            TodoActionType.TODO_TOGGLE_COMPLETE_ALL -> {
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

    private fun create(data: Pair<*, *>) {
        val id = System.currentTimeMillis()
        val todo = Todo(
            id = id,
            text = data.first as String,
            complete = data.second as Boolean
        )
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

    // Access to this method shall be from the test or private scope only.
    // このメソッドへのアクセスは、テストまたはプライベート・スコープからのみとする。
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
     fun reset() {
        todos.clear()
        lastDeleted = null
    }

    class TodoStoreChangeEvent : StoreChangeEvent
}