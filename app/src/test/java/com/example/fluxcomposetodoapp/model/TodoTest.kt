package com.example.fluxcomposetodoapp.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TodoTest {

    @Test
    fun clone_should_create_new_instance_with_same_values() {
        val original = Todo(id = 1L, text = "Test Todo", complete = true)
        val cloned = original.clone()

        assertThat(cloned).isNotSameInstanceAs(original) // Confirmation that it is not the same instance.(同じインスタンスではないことを確認)
        assertThat(cloned.id).isEqualTo(original.id)
        assertThat(cloned.text).isEqualTo(original.text)
        assertThat(cloned.complete).isEqualTo(original.complete)
    }

    @Test
    fun compareTo_should_sort_by_id() {
        val todo1 = Todo(id = 1L, text = "First")
        val todo2 = Todo(id = 2L, text = "Second")
        val todo3 = Todo(id = 1L, text = "Another First")

        assertThat(todo1.compareTo(todo2)).isEqualTo(-1)
        assertThat(todo2.compareTo(todo1)).isEqualTo(1)
        assertThat(todo1.compareTo(todo3)).isEqualTo(0)
    }

}