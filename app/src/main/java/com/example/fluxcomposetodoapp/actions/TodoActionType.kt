package com.example.fluxcomposetodoapp.actions

enum class TodoActionType(val type: String) {
    TODO_CREATE("todo-create"),
    TODO_COMPLETE("todo-complete"),
    TODO_DESTROY("todo-destroy"),
    TODO_DESTROY_COMPLETED("todo-destroy-completed"),
    TODO_TOGGLE_COMPLETE_ALL("todo-toggle-complete-all"),
    TODO_UNDO_COMPLETE("todo-undo-complete"),
    TODO_UNDO_DESTROY("todo-undo-destroy")
}