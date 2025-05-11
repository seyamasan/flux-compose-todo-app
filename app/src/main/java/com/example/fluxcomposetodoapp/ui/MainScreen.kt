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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.example.fluxcomposetodoapp.model.Todo
import com.example.fluxcomposetodoapp.stores.TodoStore
import com.example.fluxcomposetodoapp.ui.theme.FluxComposeTodoAppTheme
import com.example.fluxcomposetodoapp.utility.ValidateInputUtility
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAddClick: (Pair<String, Boolean>) -> Unit,
    onDestroyClick: (Long) -> Unit,
    onUndoDestroyClick: () -> Unit,
    onCheckedChange: (Todo) -> Unit,
    onMainCheckedChange: () -> Unit,
    onClearCompletedClick: () -> Unit,
    todoStore: TodoStore
) {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    var inputText by rememberSaveable { mutableStateOf("") }
    var itemList by rememberSaveable { mutableStateOf<List<Todo>>(listOf()) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Subscribe to TodoStore change events.
    // TodoStoreの変更イベントを購読
    LaunchedEffect(Unit) {
        todoStore.storeChangeFlow.collect { _ ->
            // Substituting a copy allows recomposition.
            // コピーしたものを代入すると再コンポーズできる
            itemList = todoStore.getTodos().map { it.copy() }

            if (todoStore.canUndo()) {
                scope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "Element deleted.",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> { onUndoDestroyClick() }
                        SnackbarResult.Dismissed -> { todoStore.resetLastDeleted() }
                    }
                }
            }
        }
    }

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.semantics { contentDescription = "Todo All Check Box" },
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        onMainCheckedChange()
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f),
                    placeholder = { Text(text = "Enter your Todo.") },
                    singleLine = true // Line breaks are invalid. 改行は無効
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (ValidateInputUtility.checkEmpty(inputText)) {
                            onAddClick(Pair(inputText, isChecked))
                            inputText = ""
                        }
                    }
                ) {
                    Text(text = "Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(itemList) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                modifier = Modifier.semantics { contentDescription = "Todo Check Box" },
                                checked = item.complete,
                                onCheckedChange = { onCheckedChange(item) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item.text)
                        }
                        IconButton(
                            onClick = { onDestroyClick(item.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Destroy todo",
                                tint = Color.Red
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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
            onAddClick = {},
            onDestroyClick = {},
            onUndoDestroyClick = {},
            onCheckedChange = {},
            onMainCheckedChange = {},
            onClearCompletedClick = {},
            todoStore = TodoStore(Dispatcher.get())
        )
    }
}