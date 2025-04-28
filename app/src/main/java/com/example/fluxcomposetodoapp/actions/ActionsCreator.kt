package com.example.fluxcomposetodoapp.actions

import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo

class ActionsCreator private constructor(
    private val dispatcher: Dispatcher
) {
    companion object {
        private var instance: ActionsCreator? = null

        fun get(dispatcher: Dispatcher): ActionsCreator {
            return instance ?: synchronized(this) {
                instance ?: ActionsCreator(dispatcher).also { instance = it }
            }
        }
    }

    suspend fun create(text: String) {
        dispatcher.dispatch(
            TodoActions.TODO_CREATE,
            TodoActions.KEY_TEXT,
            text
        )
    }

    suspend fun destroy(id: Long) {
        dispatcher.dispatch(
            TodoActions.TODO_DESTROY,
            TodoActions.KEY_ID,
            id
        )
    }

    suspend fun undoDestroy() {
        dispatcher.dispatch(
            TodoActions.TODO_UNDO_DESTROY
        )
    }

    suspend fun toggleComplete(todo: Todo) {
        val id = todo.id
        val actionType = if (todo.complete) {
            TodoActions.TODO_UNDO_COMPLETE
        } else {
            TodoActions.TODO_COMPLETE
        }

        dispatcher.dispatch(
            actionType,
            TodoActions.KEY_ID,
            id
        )
    }

    suspend fun toggleCompleteAll() {
        dispatcher.dispatch(TodoActions.TODO_TOGGLE_COMPLETE_ALL)
    }

    suspend fun destroyCompleted() {
        dispatcher.dispatch(TodoActions.TODO_DESTROY_COMPLETED)
    }
}