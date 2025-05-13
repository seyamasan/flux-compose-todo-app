package com.example.fluxcomposetodoapp.ui

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.fluxcomposetodoapp.actions.ActionsCreator
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.stores.TodoStore
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val dispatcher = Dispatcher.get()
        val actionsCreator = ActionsCreator.get(dispatcher)
        val todoStore = TodoStore.get(dispatcher)
        todoStore.reset()

        composeTestRule.setContent {
            FluxComposeTodoAppTheme {
                MainScreen(
                    onAddClick = {
                        actionsCreator.create(data = Pair(it.first, it.second))
                    },
                    onDestroyClick = {
                        actionsCreator.destroy(id = it)
                    },
                    onUndoDestroyClick = {
                        actionsCreator.undoDestroy()
                    },
                    onCheckedChange = {
                        actionsCreator.toggleComplete(it)
                    },
                    onMainCheckedChange = {
                        actionsCreator.toggleCompleteAll()
                    },
                    onClearCompletedClick = {
                        actionsCreator.destroyCompleted()
                    },
                    todoStore = todoStore
                )
            }
        }
    }

    @Test
    fun should_add_todo_when_input_text_and_click_add_button() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Test Add Todo")

        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo All Check Box")
            .assertCountEquals(1)

        composeTestRule
            .onNodeWithContentDescription("Todo All Check Box")
            .assertIsOff()

        composeTestRule
            .onAllNodesWithText("Test Add Todo")
            .assertCountEquals(1)

        composeTestRule
            .onAllNodesWithContentDescription("Destroy todo")
            .assertCountEquals(1)

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .assertCountEquals(1)

        composeTestRule
            .onNodeWithContentDescription("Todo Check Box")
            .assertIsOff()
    }

    @Test
    fun should_add_empty_todo_when_input_empty_text_and_click_add_button() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("")

        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Destroy todo")
            .assertCountEquals(0)

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .assertCountEquals(0)
    }

    @Test
    fun should_delete_todo_when_click_destroy_button() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Test Delete Todo")

        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Destroy todo")
            .onLast()
            .performClick()

        composeTestRule
            .onAllNodesWithText("Test Delete Todo")
            .assertCountEquals(0)
    }

    @Test
    fun should_toggle_todo_when_click_checkbox() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Test Toggle Todo")

        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .onLast()
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .onLast()
            .assertIsOn()
    }

    @Test
    fun should_toggle_all_todos_when_click_all_checkbox() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Todo 1")
        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Todo 2")
        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Todo All Check Box")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .assertAll(isOn())

        composeTestRule
            .onNodeWithContentDescription("Todo All Check Box")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .assertAll(isOff())
    }

    @Test
    fun should_clear_completed_todos() {
        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Completed Todo")
        composeTestRule
            .onNodeWithText("Add")
            .performClick()
        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .onLast()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Input Text Field")
            .performTextInput("Incomplete Todo")
        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onNodeWithText("Clear completed")
            .performClick()

        composeTestRule
            .onAllNodesWithText("Completed Todo")
            .assertCountEquals(0)

        composeTestRule
            .onAllNodesWithText("Incomplete Todo")
            .assertCountEquals(1)
    }

    @Test
    fun should_undo_deleted_todo() {
        composeTestRule
            .onNodeWithText("Enter your Todo.")
            .performTextInput("Undo Deleted Todo")
        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Destroy todo")
            .onLast()
            .performClick()

        composeTestRule
            .onNodeWithText("Undo")
            .performClick()

        composeTestRule
            .onAllNodesWithText("Undo Deleted Todo")
            .assertCountEquals(1)
    }
}