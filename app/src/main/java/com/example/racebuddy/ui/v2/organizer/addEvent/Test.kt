package com.example.racebuddy.ui.v2.organizer.addEvent

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DynamicTextFieldRows() {
    val rows = remember { mutableStateListOf<List<MutableState<String>>>() }

    // Initialize with one row if empty
    if (rows.isEmpty()) {
        rows.add(List(3) { mutableStateOf("") })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEachIndexed { columnIndex, textState ->
                    TextField(
                        value = textState.value,
                        onValueChange = { newText -> textState.value = newText },
                        modifier = Modifier
                            .weight(1f),
                        label = { Text("Row ${rowIndex + 1}, Col ${columnIndex + 1}") }
                    )
                }

                // Trashcan icon to remove the row
                IconButton(
                    onClick = {
                        rows.removeAt(rowIndex)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Row",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                rows.add(List(3) { mutableStateOf("") })
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("+ Add Row")
        }
    }
}

@Preview
@Composable
fun DynamicTextFieldRowsPreview() {
    DynamicTextFieldRows()
}