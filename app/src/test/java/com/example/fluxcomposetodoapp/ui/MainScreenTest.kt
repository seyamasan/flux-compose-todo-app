package com.example.fluxcomposetodoapp.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.fluxcomposetodoapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun should_add_todo_when_input_text_and_click_add_button() {

        composeTestRule
            .onNodeWithText("Enter your Todo.")
            .performTextInput("Test Todo")

        composeTestRule
            .onNodeWithText("Add")
            .performClick()

        composeTestRule
            .onAllNodesWithContentDescription("Todo All Check Box")
            .assertCountEquals(1)

        composeTestRule
            .onAllNodesWithText("Test Todo")
            .assertCountEquals(1)

        composeTestRule
            .onAllNodesWithContentDescription("Destroy todo")
            .assertCountEquals(1)

        composeTestRule
            .onAllNodesWithContentDescription("Todo Check Box")
            .assertCountEquals(1)
    }
}