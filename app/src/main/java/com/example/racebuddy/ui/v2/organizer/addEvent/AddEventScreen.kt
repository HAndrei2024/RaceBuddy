package com.example.racebuddy.ui.v2.organizer.addEvent

import android.graphics.Paint.Align
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.CustomTextField
import com.example.racebuddy.ui.v2.event.FilterButtons
import com.example.racebuddy.ui.v2.signup.BirthdateInputFields
import com.example.racebuddy.ui.v2.signup.BirthdatePicker
import com.example.racebuddy.ui.v2.signup.CountrySelectorWithFlags
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

@Composable
fun AddEventScreen(
    eventCategories: List<String> = listOf("Road", "XC", "XCO", "Enduro", "Downhill"),
    selectedEventCategory: String,
    onFilterCategoryButtonClick: (category: String) -> Unit,
    titleTextStringValue: String,
    onCountryTextFieldChange: (country: String) -> Unit,
    cityTextFieldValue: String,
    onCityValueChange: (city: String) -> Unit,
    countyTextFieldValue: String,
    onCountyValueChange: (city: String) -> Unit,
    onTitleValueChange: (String) -> Unit,
    startDayValue: String,
    onStartDayValueChange: (String) -> Unit,
    startMonthValue: String,
    onStartMonthValueChange: (String) -> Unit,
    endDayValue: String,
    onEndDayValueChange: (String) -> Unit,
    endMonthValue: String,
    onEndMonthValueChange: (String) -> Unit,
    detailsTextFieldValue: String,
    onDetailsValueChange: (String) -> Unit,
    athleteCategoryRows: List<List<String>>,
    updateCategoryRowCell: (rowIndex: Int, colIndex: Int, value: String) -> Unit,
    addAthleteCategoryRow: () -> Unit,
    removeAthleteCategoryRow: (index: Int) -> Unit,
    isAddButtonEnabled: Boolean,
    onBackClick: () -> Unit,
    onAddEventClick: () -> Unit,
    showError: Boolean,
    errorMessage: String
) {
    Scaffold(
        topBar = {
//            CustomTopAppBar(
//                title = "Add Event",
//                onBackClick = onBackClick
//            )
            CustomTopAppBarMaterial3(
                title = "Add Event",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarWithButtonUsingShadow(
                buttonText = "Add",
                onClick = onAddEventClick,
                isEnabled = isAddButtonEnabled,
                showError = showError,
                errorMessage = errorMessage
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                InputBlock(
                    title = "Title"
                ) {
                    TitleBlock(
                        titleTextStringValue = titleTextStringValue,
                        onTitleValueChange = onTitleValueChange,
                        modifier = Modifier
                    )
                }
            }

            item {
                InputBlock(
                    title = "Category",
                ) {
                    EventCategories(
                        categories = eventCategories,
                        selectedCategory = selectedEventCategory,
                        onFilterButtonClick = onFilterCategoryButtonClick
                    )
                }
            }

            item {
                InputBlock(
                    title = "Information"
                ) {
                    EventInformations(
                        onCountryTextFieldChange = onCountryTextFieldChange,
                        cityTextFieldValue = cityTextFieldValue,
                        onCityValueChange = onCityValueChange,
                        countyTextFieldValue = countyTextFieldValue,
                        onCountyValueChange = onCountyValueChange,
                        modifier = Modifier
                    )
                }
            }

            item {
                InputBlock(
                    title = "Date"
                ) {
                    DateBlock(
                        startDayValue = startDayValue,
                        onStartDayValueChange = onStartDayValueChange,
                        startMonthValue = startMonthValue,
                        onStartMonthValueChange = onStartMonthValueChange,
                        endDayValue = endDayValue,
                        onEndDayValueChange = onEndDayValueChange,
                        endMonthValue = endMonthValue,
                        onEndMonthValueChange = onEndMonthValueChange,
                        modifier = Modifier
                    )
                }
            }

            item {
                InputBlock(
                    title = "Details"
                ) {
                    DetailsBlock(
                        detailsTextFieldValue = detailsTextFieldValue,
                        onDetailsValueChange = onDetailsValueChange
                    )
                }
            }

            item {
                InputBlock(
                    title = "Athlete Categories"
                ) {
                    DynamicCategoryRows(
                        rows = athleteCategoryRows,
                        updateCell = updateCategoryRowCell,
                        addRow = addAthleteCategoryRow,
                        removeRow = removeAthleteCategoryRow,
                        modifier = Modifier
                    )
                }
            }

            item {
                InputBlock(
                    title = "Background Image"
                ) {

                }
            }

            if(showError) {
                item {

                }
            }

        }

    }
}

@Composable
fun ErrorText(
    textString: String = "Incorrect email or password.",
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(start = paddings.spacingLarge)
            .fillMaxWidth()
    ) {
        Text(
            text = textString,
            style = AppTypography.bodyLarge,
            color = Color.Red
        )
    }
}

@Composable
fun InputBlock(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(paddings.spacingSmall)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(paddings.spacingMedium)
        )

        content()

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFDDDDDD),
            modifier = Modifier
                .padding(top = paddings.spacingSmall)
                .fillMaxWidth(), // respects Column's padding
        )
    }
}

@Composable
fun CustomLargeTextField(
    textStringValue: String,
    placeholderString: String,
    onValueChange: (String) -> Unit,
    supportingText:@Composable() (() -> Unit)? = null,
    onFocusChanged: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var isPlaceholder by remember { mutableStateOf(true) }



    OutlinedTextField(
        value = textStringValue,
        textStyle = AppTypography.bodyLarge.copy(
            color = if (textStringValue == placeholderString) {
                MaterialTheme.colorScheme.onSurface
            } else {
                Color.Black
            }
        ),
        placeholder = {
            Text(
                text = if(!hasFocus) placeholderString else "",
            )
        },
        onValueChange = {
            onValueChange(it)
            isPlaceholder = false
        },
        shape = shapes.small,
        supportingText = supportingText,
        modifier = modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
            )
            .onFocusChanged { focusState ->

                // If focus was lost
                if (hasFocus && !focusState.isFocused) {
                    onFocusChanged()
                }
                hasFocus = focusState.isFocused
            }
            .height(150.dp)

    )

}


@Composable
fun DetailsBlock(
    detailsTextFieldValue: String,
    onDetailsValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingLarge, end = paddings.spacingMedium)
    ) {
        CustomLargeTextField(
            textStringValue = detailsTextFieldValue,
            placeholderString = "Details",
            onValueChange = onDetailsValueChange,
            onFocusChanged = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DateBlock(
    startDayValue: String,
    onStartDayValueChange: (String) -> Unit,
    startMonthValue: String,
    onStartMonthValueChange: (String) -> Unit,
    endDayValue: String,
    onEndDayValueChange: (String) -> Unit,
    endMonthValue: String,
    onEndMonthValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingLarge, end = paddings.spacingMedium)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            BirthdateInputFields(
                titleText = "Start date",
                dayValue = startDayValue,
                onDayValueChange = onStartDayValueChange,
                yearValue = LocalDate.now().year.toString(),
                onYearValueChange = {},
                yearReadOnly = true,
                monthValue = startMonthValue,
                onMonthValueChange = onStartMonthValueChange
            )

            BirthdateInputFields(
                titleText = "End date",
                dayValue = endDayValue,
                onDayValueChange = onEndDayValueChange,
                yearValue = LocalDate.now().year.toString(),
                onYearValueChange = {},
                yearReadOnly = true,
                monthValue = endMonthValue,
                onMonthValueChange = onEndMonthValueChange
            )
        }
    }
}

@Composable
fun EventCategories(
    categories: List<String>,
    selectedCategory: String = "XC",
    onFilterButtonClick: (category: String) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = paddings.spacingSmall),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = paddings.spacingMedium)
    ) {
        items(categories) { item ->
            val isSelected = item == selectedCategory
            Button(
                onClick = {
                    //selectedItem = item
                    // TODO: Implement actual action here
                    onFilterButtonClick(item)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(2.dp),
                shape = RoundedCornerShape(shapes.small.topEnd),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = item)
            }
        }
    }
}

@Composable
fun EventInformations(
    onCountryTextFieldChange: (country: String) -> Unit,
    cityTextFieldValue: String,
    onCityValueChange: (city: String) -> Unit,
    countyTextFieldValue: String,
    onCountyValueChange: (city: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingMedium, end = paddings.spacingMedium)
    ) {


        CountrySelectorWithFlags(
            titleText = "Country",
            onNationalityTextFieldChange = onCountryTextFieldChange,
            modifier = Modifier.fillMaxWidth()
        )

        TextInputBlock(
            titleAboveText = "City",
            textStringValue = cityTextFieldValue,
            placeholderString = "Enter City...",
            iconImageVector = Icons.Default.Info,
            onValueChange = onCityValueChange
        )

        TextInputBlock(
            titleAboveText = "County",
            textStringValue = countyTextFieldValue,
            placeholderString = "Enter County...",
            iconImageVector = Icons.Default.Info,
            onValueChange = onCountyValueChange
        )
    }
}

@Composable
fun TitleBlock(
    titleTextStringValue: String,
    onTitleValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingMedium, end = paddings.spacingMedium)
    ) {
        TextInputBlock(
            titleAboveText = "Event Title",
            textStringValue = titleTextStringValue,
            placeholderString = "Enter Title...",
            iconImageVector = Icons.Default.Info,
            onValueChange = onTitleValueChange
        )
    }
}

@Composable
fun TextInputBlock(
    titleAboveText: String,
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.padding(start = paddings.spacingMedium)
    ) {
        Text(
            text = titleAboveText,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall / 2,
                    top = paddings.spacingXSmall
                )
                .offset(y = paddings.spacingXSmall)
        )
        CustomTextField(
            textStringValue = textStringValue,
            placeholderString = placeholderString,
            iconImageVector = iconImageVector,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun CustomTopAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    Surface(
        shadowElevation = 4.dp, // Elevation for shadow
        color = Color.White, // Background color,
        modifier = Modifier
            //.padding(top = paddings.spacingLarge)
            .height(75.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Default app bar height
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Centered title
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBarMaterial3(
    title: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
    )
}


@Composable
fun BottomBarWithButtonUsingShadow(
    buttonText: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    showError: Boolean,
    errorMessage: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if(showError) {
            ErrorText(
                textString = errorMessage
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp) // Applies visual shadow all around
                .background(Color.White)
                .padding(paddings.spacingMedium)
                .zIndex(2f)
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                enabled = isEnabled,
                shape = shapes.small,
                modifier = Modifier
                    //.fillMaxWidth()
                    .height(48.dp)
                    .width(281.dp)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    style = AppTypography.bodyLarge.copy(
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun InputBlockPreview() {
    InputBlock(
        title = "Test",
    ) {
        com.example.racebuddy.ui.v2.main.FilterButtons {  }
    }
}

@Composable
fun AddCategoryRowHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = paddings.spacingSmall, alignment = Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingSmall, end = paddings.spacingSmall)

    ) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall / 2,
                    top = paddings.spacingXSmall
                )
                //.offset(y = paddings.spacingXSmall)
                .weight(3f)
        )

        Text(
            text = "Min Age",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall / 2,
                    top = paddings.spacingXSmall
                )
                //.offset(y = paddings.spacingXSmall)
                .weight(1f)
        )

        Text(
            text = "Max Age",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall / 2,
                    top = paddings.spacingXSmall
                )
                //.offset(y = paddings.spacingXSmall)
                .weight(1f)
        )

        Text(
            text = "",
            modifier = Modifier
                .weight(0.5f)
        )
    }
}

@Composable
fun AddCategoryRow(
    categoryStringValue: String,
    onCategoryValueChange: (String) -> Unit,
    mininumAgeStringValue: String,
    onMinimumAgeValueChange: (String) -> Unit,
    maximumAgeStringValue: String,
    onMaximumAgeValueChange: (String) -> Unit,
    removeRow: () -> Unit,
    ) {
    val maxAgeFocusRequester = remember { FocusRequester() }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = paddings.spacingSmall, alignment = Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingSmall, end = paddings.spacingSmall)
    ) {
        CustomTextField(
            textStringValue = categoryStringValue,
            placeholderString = "Category",
            iconImageVector = Icons.Default.Person,
            onValueChange = onCategoryValueChange,
            onFocusChanged = {},
            modifier = Modifier
                .weight(3f)
        )

        OutlinedTextField(
            value = mininumAgeStringValue,
            onValueChange = {
                if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                    // Update Month TODO
                    onMinimumAgeValueChange(it)
                    // If month has 2 digits, move focus to the year field
                    if (it.length == 2) {
                        maxAgeFocusRequester.requestFocus()
                    }
                }
            },
            //label = { Text("MM") },
            placeholder = { Text("YY") },
            textStyle = AppTypography.bodyLarge.copy(
                color = if (mininumAgeStringValue == "YY") {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    Color.Black
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { maxAgeFocusRequester.requestFocus() }
            ),
            modifier = Modifier
                //.width(80.dp) // Fixed width for consistency
                //.focusRequester(monthFocusRequester) // Assign focus requester for the month input
                .weight(1f)
        )

        OutlinedTextField(
            value = maximumAgeStringValue,
            onValueChange = {
                if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                    // Update Month TODO
                    onMaximumAgeValueChange(it)
                    // If month has 2 digits, move focus to the year field
                    if (it.length == 2) {
                        //maxAgeFocusRequester.requestFocus()
                    }
                }
            },
            //label = { Text("MM") },
            placeholder = { Text("YY") },
            textStyle = AppTypography.bodyLarge.copy(
                color = if (maximumAgeStringValue == "YY") {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    Color.Black
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                //onNext = { maxAgeFocusRequester.requestFocus() }
            ),
            modifier = Modifier
                //.width(80.dp) // Fixed width for consistency
                .focusRequester(maxAgeFocusRequester) // Assign focus requester for the month input
                .weight(1f)
        )

        IconButton(
            onClick =  removeRow,
            modifier = Modifier
                .weight(0.5f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Row",
                tint = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
fun DynamicCategoryRows(
    rows: List<List<String>>,
    updateCell: (rowIndex: Int, colIndex: Int, value: String) -> Unit,
    addRow: () -> Unit,
    removeRow: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(paddings.spacingSmall)
            .fillMaxWidth()
    ) {
        AddCategoryRowHeader()

        rows.forEachIndexed { index, row ->
            var visible by remember(index) { mutableStateOf(true) }
            var shouldDelete by remember(index) { mutableStateOf(false) }


            if (shouldDelete) {
                // Run this effect after visible = false
                LaunchedEffect(Unit) {
                    delay(250) // Give time for animation
                    removeRow(index)
                }
            }


            AnimatedVisibility(
                visible = visible,
                exit = fadeOut() + shrinkVertically(),
                enter = fadeIn() + expandVertically()
            ) {
                AddCategoryRow(
                    categoryStringValue = row[0],
                    onCategoryValueChange = {
                        updateCell(index, 0, it)
                    },
                    mininumAgeStringValue = row[1],
                    onMinimumAgeValueChange = {
                        updateCell(index, 1, it)
                    },
                    maximumAgeStringValue = row[2],
                    onMaximumAgeValueChange = {
                        updateCell(index, 2, it)
                    },
                    removeRow = {
                        visible = false
                        shouldDelete = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }


        Button(
            onClick = {
                addRow()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = shapes.small,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("+ Add Row")
        }
    }
}



@Preview
@Composable
fun AddCategoryRowPreview() {
    Column() {
        AddCategoryRowHeader()
        AddCategoryRow(
            categoryStringValue = "Junior",
            onCategoryValueChange = {},
            mininumAgeStringValue = "10",
            onMinimumAgeValueChange = {},
            maximumAgeStringValue = "18",
            onMaximumAgeValueChange = {},
            removeRow = {}
        )
    }
}

@Preview
@Composable
fun AddEventScreenPreview() {
    AddEventScreen(
        eventCategories = listOf("Road", "XC", "XCO", "Enduro", "Downhill"),
        selectedEventCategory = "",
        onFilterCategoryButtonClick = {},
        titleTextStringValue = "",
        onCountryTextFieldChange = {},
        cityTextFieldValue = "",
        onCityValueChange = {},
        countyTextFieldValue = "",
        onCountyValueChange = {},
        onTitleValueChange = {},
        startDayValue = "",
        onStartDayValueChange = {},
        startMonthValue = "",
        onStartMonthValueChange = {},
        endDayValue = "",
        onEndDayValueChange = {},
        endMonthValue = "",
        onEndMonthValueChange = {},
        detailsTextFieldValue = "",
        onDetailsValueChange = {},
        athleteCategoryRows = listOf(List(3) { "" }),
        updateCategoryRowCell = {rowIndex: Int, colIndex: Int, value: String ->},
        addAthleteCategoryRow = {},
        removeAthleteCategoryRow = {},
        onBackClick = {},
        isAddButtonEnabled = false,
        onAddEventClick = {},
        showError = true,
        errorMessage = "lalal"
    )
}



