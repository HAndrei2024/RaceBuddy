package com.example.racebuddy.ui.v2.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.now

@Composable
fun TestDateScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        BirthdateInputs3()
    }
}



@Composable()
fun TestDate() {
    var showDatePicker by remember { mutableStateOf(false)}
    var stringValue by remember { mutableStateOf("")}


    TextField(
        value = stringValue,
        onValueChange = {},
        modifier = Modifier
            .clickable{
                showDatePicker = true
            },
        enabled = false
    )

    WheelDatePickerView(
        showDatePicker = showDatePicker,
        height = 200.dp,
        dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
        rowCount = 3,
        titleStyle = MaterialTheme.typography.bodyLarge,
        doneLabelStyle = MaterialTheme.typography.bodyLarge,
        dateTextStyle = MaterialTheme.typography.bodyMedium,
        onDoneClick = {
            showDatePicker = false
        },
        onDismiss = {
            showDatePicker = false
        },
        yearsRange = 1950..LocalDate.now().year
    )
}
@Composable
fun BirthdateTextField() {
    var text by remember { mutableStateOf("") }

    // Format the input as DD/MM/YYYY
    val formattedText = remember(text) {
        formatDate(text)
    }

    // TextField for input
    OutlinedTextField(
        value = formattedText,
        onValueChange = { newText ->
            // Ensure the text is valid and matches the DD/MM/YYYY format
            if (isValidDate(newText) || newText.length <= 10) { // Limit length to avoid overflow
                text = newText
            }
        },
        label = { Text("Birthdate") },  // Optional label for the TextField
        placeholder = { Text("DD/MM/YYYY") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions.Default,
        modifier = Modifier.fillMaxWidth()
    )
}

// Format the input to DD/MM/YYYY and insert '/' between day, month, and year
fun formatDate(input: String): String {
    val cleanInput = input.filter { it.isDigit() }

    return when {
        cleanInput.length in 3..4 -> {
            // Add '/' between day and month
            cleanInput.take(2) + "/" + cleanInput.drop(2).take(2)
        }
        cleanInput.length in 5..6 -> {
            // Add '/' between day and month
            cleanInput.take(2) + "/" + cleanInput.drop(2).take(2) + "/" + cleanInput.drop(4).take(4)
        }
        else -> cleanInput
    }
}

// Validate if the date is in DD/MM/YYYY format and the date is valid
fun isValidDate(input: String): Boolean {
    val regex = """\d{2}/\d{2}/\d{4}""".toRegex()  // Ensure input matches DD/MM/YYYY format
    if (!input.matches(regex)) return false

    val parts = input.split("/")
    val day = parts[0].toInt()
    val month = parts[1].toInt()
    val year = parts[2].toInt()

    // Check if the day is valid for the given month
    if (month !in 1..12) return false  // Month must be between 01 and 12

    // Define the number of days for each month (ignoring leap years for simplicity)
    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> 28  // Not accounting for leap years for simplicity
        else -> 0
    }

    // Ensure the day is within valid range for the given month
    return day in 1..daysInMonth
}


@Composable
fun BirthdateInputs() {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    // Function to validate and format the inputs
    fun isValidDay(day: String) = day.toIntOrNull()?.let { it in 1..31 } == true
    fun isValidMonth(month: String) = month.toIntOrNull()?.let { it in 1..12 } == true
    fun isValidYear(year: String) = year.length == 4 && year.toIntOrNull() != null

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between fields
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Day input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = day,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        day = it
                    }
                },
                label = { Text("DD") },
                isError = !isValidDay(day),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(80.dp) // Fixed width for consistency
            )
        }

        // Month input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = month,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        month = it
                    }
                },
                label = { Text("MM") },
                isError = !isValidMonth(month),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(80.dp) // Fixed width for consistency
            )
        }

        // Year input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = year,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) { // Allow up to 4 digits
                        year = it
                    }
                },
                label = { Text("YYYY") },
                isError = !isValidYear(year),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp) // Fixed width for consistency
            )
        }
    }
}

@Composable
fun BirthdateInputs2() {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    // Create focus requesters for each TextField
    val dayFocusRequester = remember { FocusRequester() }
    val monthFocusRequester = remember { FocusRequester() }
    val yearFocusRequester = remember { FocusRequester() }

    // Function to validate and format the inputs
    fun isValidDay(day: String) = day.toIntOrNull()?.let { it in 1..31 } == true
    fun isValidMonth(month: String) = month.toIntOrNull()?.let { it in 1..12 } == true
    fun isValidYear(year: String) = year.length == 4 && year.toIntOrNull() != null

    // Row to arrange the inputs in a horizontal row
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between fields
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Day input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = day,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        day = it
                        // If day has 2 digits, move focus to the month field
                        if (it.length == 2) {
                            monthFocusRequester.requestFocus()
                        }
                    }
                },
                label = { Text("DD") },
                isError = !isValidDay(day),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { monthFocusRequester.requestFocus() } // Focus on month when Enter is pressed
                ),
                modifier = Modifier
                    .width(80.dp) // Fixed width for consistency
                    .focusRequester(dayFocusRequester) // Assign focus requester for the day input
            )
        }

        // Month input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = month,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        month = it
                        // If month has 2 digits, move focus to the year field
                        if (it.length == 2) {
                            yearFocusRequester.requestFocus()
                        }
                    }
                },
                label = { Text("MM") },
                isError = !isValidMonth(month),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { yearFocusRequester.requestFocus() } // Focus on year when Enter is pressed
                ),
                modifier = Modifier
                    .width(80.dp) // Fixed width for consistency
                    .focusRequester(monthFocusRequester) // Assign focus requester for the month input
            )
        }

        // Year input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = year,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) { // Allow up to 4 digits
                        year = it
                    }
                },
                label = { Text("YYYY") },
                isError = !isValidYear(year),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle Done action, e.g., hide keyboard */ }
                ),
                modifier = Modifier
                    .width(100.dp) // Fixed width for consistency
                    .focusRequester(yearFocusRequester) // Assign focus requester for the year input
            )
        }
    }
}

@Composable
fun BirthdateInputs3() {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    val currentYear = LocalDate.now().year
    val minYear = 1920

    // Create focus requesters for each TextField
    val dayFocusRequester = remember { FocusRequester() }
    val monthFocusRequester = remember { FocusRequester() }
    val yearFocusRequester = remember { FocusRequester() }

    // Functions to validate the inputs
    fun isValidDay(day: String): Boolean {
        val dayInt = day.toIntOrNull()
        return dayInt != null && dayInt in 1..31
    }

    fun isValidMonth(month: String): Boolean {
        val monthInt = month.toIntOrNull()
        return monthInt != null && monthInt in 1..12
    }

    fun isValidYear(year: String): Boolean {
        val yearInt = year.toIntOrNull()
        return yearInt != null && yearInt in minYear..currentYear
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between fields
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Day input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = day,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        day = it
                        // If day has 2 digits, move focus to the month field
                        if (it.length == 2) {
                            monthFocusRequester.requestFocus()
                        }
                    }
                },
                label = { Text("DD") },
                isError = day.isNotEmpty() && !isValidDay(day), // Error state is only true when there is invalid input
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { monthFocusRequester.requestFocus() }
                ),
                modifier = Modifier
                    .width(80.dp) // Fixed width for consistency
                    .focusRequester(dayFocusRequester) // Assign focus requester for the day input
            )
        }

        // Month input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = month,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        month = it
                        // If month has 2 digits, move focus to the year field
                        if (it.length == 2) {
                            yearFocusRequester.requestFocus()
                        }
                    }
                },
                label = { Text("MM") },
                isError = month.isNotEmpty() && !isValidMonth(month), // Error state is only true when there is invalid input
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { yearFocusRequester.requestFocus() }
                ),
                modifier = Modifier
                    .width(80.dp) // Fixed width for consistency
                    .focusRequester(monthFocusRequester) // Assign focus requester for the month input
            )
        }

        // Year input
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = year,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) { // Allow up to 4 digits
                        year = it
                    }
                },
                label = { Text("YYYY") },
                isError = year.isNotEmpty() && !isValidYear(year), // Error state is only true when there is invalid input
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle Done action, e.g., hide keyboard */ }
                ),
                modifier = Modifier
                    .width(100.dp) // Fixed width for consistency
                    .focusRequester(yearFocusRequester) // Assign focus requester for the year input
            )
        }
    }
}

