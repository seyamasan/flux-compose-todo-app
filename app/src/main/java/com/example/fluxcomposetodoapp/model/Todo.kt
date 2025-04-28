package com.example.fluxcomposetodoapp.model

data class Todo(
    val id: Long,
    val text: String,
    var complete: Boolean = false
) : Cloneable, Comparable<Todo> {

    public override fun clone(): Todo {
        return Todo(id, text, complete)
    }

    override fun compareTo(other: Todo): Int {
        return when {
            id == other.id -> 0
            id < other.id -> -1
            else -> 1
        }
    }
}