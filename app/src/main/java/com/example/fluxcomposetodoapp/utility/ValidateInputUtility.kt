package com.example.fluxcomposetodoapp.utility

object ValidateInputUtility {
    fun checkEmpty(inputText: String): Boolean {
        return inputText.isNotEmpty()
    }
}