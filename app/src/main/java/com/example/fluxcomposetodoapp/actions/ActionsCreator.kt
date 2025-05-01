package com.example.fluxcomposetodoapp.actions

import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo

/**
 * ActionsCreator
 * Pass operations from the View to the Dispatcher.
 * Viewからの操作をDispatcherに渡す
 **/
class ActionsCreator private constructor(
    private val dispatcher: Dispatcher
) {
    companion object {
        private var instance: ActionsCreator? = null

        fun get(dispatcher: Dispatcher): ActionsCreator {
            // Synchronized to control exclusivity so that there are not multiple instances. Singleton is realized.
            // synchronizedでインスタンスが複数にならないように排他制御をする。シングルトンを実現。
            return instance ?: synchronized(this) {
                instance ?: ActionsCreator(dispatcher).also { instance = it }
            }
        }
    }

    suspend fun create(data: Pair<String, Boolean>) {
        dispatcher.dispatch(
            createAction(
                TodoActionType.TODO_CREATE,
                hashMapOf(TodoActionKeys.KEY_TEXT to data)
            )
        )
    }

    suspend fun destroy(id: Long) {
        dispatcher.dispatch(
            createAction(
                type = TodoActionType.TODO_DESTROY,
                data = hashMapOf(TodoActionKeys.KEY_ID to id)
            )
        )
    }

    suspend fun undoDestroy() {
        dispatcher.dispatch(
            createAction(
                type = TodoActionType.TODO_UNDO_DESTROY,
                data = null
            )
        )
    }

    suspend fun toggleComplete(todo: Todo) {
        val id = todo.id
        val actionType = if (todo.complete) {
            TodoActionType.TODO_UNDO_COMPLETE
        } else {
            TodoActionType.TODO_COMPLETE
        }

        dispatcher.dispatch(
            createAction(
                type = actionType,
                data = hashMapOf(TodoActionKeys.KEY_ID to id)
            )
        )
    }

    suspend fun toggleCompleteAll() {
        dispatcher.dispatch(
            createAction(
                type = TodoActionType.TODO_TOGGLE_COMPLETE_ALL,
                data = null
            )
        )
    }

    suspend fun destroyCompleted() {
        dispatcher.dispatch(
            createAction(
                type = TodoActionType.TODO_DESTROY_COMPLETED,
                data = null
            )
        )
    }

    private fun createAction(type: TodoActionType, data: HashMap<String, Any>?):  Action {
        val actionBuilder = Action.type(type)

        data?.let {
            it.forEach { (key, value) ->
                actionBuilder.setData(key, value)
            }
        }

        return actionBuilder.build()
    }
}