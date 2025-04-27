package com.example.fluxcomposetodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fluxcomposetodoapp.ui.MainScreen
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FluxComposeTodoAppTheme {
                MainScreen(
                    inputText = "Test input",
                    onInputChange = {},
                    isChecked = false,
                    onCheckedChange = {},
                    onAddClick = {},
                    onClearCompletedClick = {},
                    itemList = listOf()
                )
            }
        }
    }
}