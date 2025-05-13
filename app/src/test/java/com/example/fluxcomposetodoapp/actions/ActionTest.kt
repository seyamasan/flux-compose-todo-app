package com.example.fluxcomposetodoapp.actions

import org.junit.Test
import org.junit.Assert.*

class ActionTest {

    @Test
    fun should_create_action_with_type() {
        val action = Action.type(TodoActionType.TODO_CREATE).build()

        assertEquals(TodoActionType.TODO_CREATE, action.type)
        assertTrue(action.data.isEmpty())
    }

    @Test
    fun should_create_action_with_type_and_data() {
        val key = "test_key"
        val value = "test_value"

        val action = Action.type(TodoActionType.TODO_CREATE)
            .apply { setData(key, value) }
            .build()

        assertEquals(TodoActionType.TODO_CREATE, action.type)
        assertEquals(value, action.data[key])
    }

    @Test(expected = IllegalArgumentException::class)
    fun should_throw_exception_when_type_is_null() {
        Action.Builder().build()
    }
}