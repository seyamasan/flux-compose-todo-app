package com.example.fluxcomposetodoapp.utility

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateInputUtilityTest {

    @Test
    fun should_return_false_when_input_is_empty() {
        val emptyInput = ""

        val result = ValidateInputUtility.checkEmpty(emptyInput)

        assertThat(result).isFalse()
    }

    @Test
    fun should_return_true_when_input_has_text() {
        val validInput = "Test text."

        val result = ValidateInputUtility.checkEmpty(validInput)

        assertThat(result).isTrue()
    }

}