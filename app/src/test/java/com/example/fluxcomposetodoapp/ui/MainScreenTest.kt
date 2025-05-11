package com.example.fluxcomposetodoapp.ui

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.fluxcomposetodoapp.MainActivity
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

/**
 * @FixMethodOrder(MethodSorters.NAME_ASCENDING)
 * Sorts the test methods by the method name, in lexicographic order, with Method. toString() used as a tiebreaker.
 * メソッド名でテストメソッドの実行順を辞書順に並べ替えてくれる。
 **/
@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun a1_should_add_todo_when_input_text_and_click_add_button() {
        printlnTestFuncName(name = "a1_should_add_todo_when_input_text_and_click_add_button")

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
    fun a2_should_delete_todo_when_click_destroy_button() {
        printlnTestFuncName(name = "a2_should_add_todo_when_input_text_and_click_add_button")

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
    fun a3_should_toggle_todo_when_click_checkbox() {
        printlnTestFuncName(name = "a3_should_add_todo_when_input_text_and_click_add_button")

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
    fun a4_should_toggle_all_todos_when_click_all_checkbox() {
        printlnTestFuncName(name = "a4_should_add_todo_when_input_text_and_click_add_button")

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
//
//    @Test
//    fun a5_should_clear_completed_todos() {
//        printlnTestFuncName(name = "a5_should_clear_completed_todos")
//
//        composeTestRule
//            .onNodeWithContentDescription("Input Text Field")
//            .performTextInput("Completed Todo")
//        composeTestRule
//            .onNodeWithText("Add")
//            .performClick()
//        composeTestRule
//            .onAllNodesWithContentDescription("Todo Check Box")
//            .onLast()
//            .performClick()
//
//        composeTestRule
//            .onNodeWithContentDescription("Input Text Field")
//            .performTextInput("Incomplete Todo")
//        composeTestRule
//            .onNodeWithText("Add")
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText("Clear completed")
//            .performClick()
//
//        composeTestRule
//            .onAllNodesWithText("Completed Todo")
//            .assertCountEquals(0)
//
//        composeTestRule
//            .onAllNodesWithText("Incomplete Todo")
//            .assertCountEquals(1)
//    }
//
//    @Test
//    fun a6_should_undo_deleted_todo() {
//        printlnTestFuncName(name = "a6_should_clear_completed_todos")
//
//        composeTestRule
//            .onNodeWithText("Enter your Todo.")
//            .performTextInput("Undo Deleted Todo")
//        composeTestRule
//            .onNodeWithText("Add")
//            .performClick()
//
//        composeTestRule
//            .onAllNodesWithContentDescription("Destroy todo")
//            .onLast()
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText("Undo")
//            .performClick()
//
//        composeTestRule
//            .onAllNodesWithText("Undo Deleted Todo")
//            .assertCountEquals(1)
//    }

    private fun printlnTestFuncName(name: String) {
        println("==========================================")
        println("Test starting: $name")
        println("==========================================")
    }
}