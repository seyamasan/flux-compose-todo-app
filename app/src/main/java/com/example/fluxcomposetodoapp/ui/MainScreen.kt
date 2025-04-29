package com.example.fluxcomposetodoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluxcomposetodoapp.model.Todo
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    inputText: String,
    onInputChange: (String) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onAddClick: () -> Unit,
    onClearCompletedClick: () -> Unit,
    itemList: List<Todo>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Todo App",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
         },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text(text = "Enter your Todo.") },
                    singleLine = true // Line breaks are invalid. 改行は無効
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onAddClick
                ) {
                    Text(text = "Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(itemList) { item ->
                    Text(
                        text = item.text,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            }


            Button(
                onClick = onClearCompletedClick,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Clear completed")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
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