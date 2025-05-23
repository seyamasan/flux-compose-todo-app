package com.example.fluxcomposetodoapp.actions

enum class TodoActionType {
    TODO_CREATE,
    TODO_COMPLETE,
    TODO_DESTROY,
    TODO_DESTROY_COMPLETED,
    TODO_TOGGLE_COMPLETE_ALL,
    TODO_UNDO_COMPLETE,
    TODO_UNDO_DESTROY
}