package com.example.racebuddy.ui.v2.signup

import android.util.Log
import android.widget.Button
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.CustomTextField
import com.example.racebuddy.ui.v2.login.LoginButton
import com.example.racebuddy.ui.v2.login.PasswordTextField
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.now
import kotlin.math.exp

@Composable
fun SignupSecondScreen(
    onNationalityTextFieldChange: (String) -> Unit,
    nationalityStringValue: String,
    onDayTextFieldChange: (String) -> Unit,
    dayStringValue: String,
    onMonthTextFieldChange: (String) -> Unit,
    monthStringValue: String,
    onYearTextFieldChange: (String) -> Unit,
    yearStringValue: String,
    selectedGender: String,
    onGenderButtonClick: (String) -> Unit,
    onLicenseNumberTextFieldChange: (String) -> Unit,
    licenseNumberStringValue: String,
    onBirthdateTextFieldClick: () -> Unit,
    birthdateStringValue: String,
    onEmailTexFieldChange: (String) -> Unit,
    emailStringValue: String,
    onPasswordTextFieldChange: (String) -> Unit,
    passwordStringValue: String,
    errorMessage: Boolean,
    onContinueClick: () -> Unit,
    showDatePicker: Boolean,
    onDatePickerDoneClick: () -> Unit,
    onDatePickerDissmisClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(start = paddings.spacingExtraLarge)
    ) {
        SignUpText2ndScreen()

        CountrySelectorWithFlags(
            onNationalityTextFieldChange = onNationalityTextFieldChange
        )
        //CountrySelector()

//        CustomTextField(
//            textStringValue = "",
//            placeholderString = "Username",
//            iconImageVector = Icons.Filled.Person,
//            onValueChange = {}
//        )
        BirthdateInputFields(
            dayValue = dayStringValue,
            onDayValueChange = onDayTextFieldChange,
            yearValue = yearStringValue,
            onYearValueChange = onYearTextFieldChange,
            monthValue = monthStringValue,
            onMonthValueChange = onMonthTextFieldChange
        )
//        BirthdatePicker(
//            birthdateString = "",
//            onValueChange = onBirthdateTextFieldChange,
//            onClick = onBirthdateTextFieldClick,
//            showDatePicker = showDatePicker,
//            onDatePickerDoneClick = onDatePickerDoneClick,
//            onDatePickerDissmisClick = onDatePickerDissmisClick
//        )
//        CustomTextField(
//            textStringValue = "",
//            placeholderString = "Birthdate",
//            iconImageVector = Icons.Filled.Person,
//            onValueChange = {}
//        )

//        CustomTextField(
//            textStringValue = "",
//            placeholderString = "Gender",
//            iconImageVector = Icons.Filled.Person,
//            onValueChange = {}
//        )
        GenderSelector(
            selectedGender = selectedGender,
            onGenderSelected = onGenderButtonClick
        )

        SwitchWithCustomColors(
            licenseNumberStringValue = licenseNumberStringValue,
            onLicenseNumberTextFieldChange = onLicenseNumberTextFieldChange
        )

//        CustomTextField(
//            textStringValue = "",
//            placeholderString = "Registration Number",
//            iconImageVector = Icons.Filled.Person,
//            onValueChange = {},
//        )

        SignupButton(
            onClick = onContinueClick
        )
        //PageIndicator(1)
    }
}

@Composable
fun SignUpText2ndScreen() {
    Column(
        modifier = Modifier
            .padding(bottom = paddings.spacingExtraLarge)
    ) {
        Text(
            text = "Sign Up",
            style = AppTypography.displayLarge,
            modifier = Modifier
                .padding(bottom = paddings.spacingXSmall)
        )
        Text(
            text = "Just a few more details...",
            style = TextStyle(
                color = Color.Gray,
                fontStyle = AppTypography.titleSmall.fontStyle, //gabaritoMediumBoldTextStyle
                fontSize = AppTypography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun SwitchWithCustomColors(
    licenseNumberStringValue: String,
    onLicenseNumberTextFieldChange: (String) -> Unit
) {
    var checked by remember { mutableStateOf(false) }

    //Spacer(modifier = Modifier.height(paddings.spacingSmall))
    Text(
        text = "Are you licensed?",
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(
                start = paddings.spacingXSmall/2,
                top = paddings.spacingXSmall
            )
            .offset(y = paddings.spacingXSmall)
    )

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = Color.Gray,
            checkedBorderColor = Color.Gray,
            uncheckedTrackColor = Color.LightGray,
            uncheckedBorderColor = Color.LightGray
        ),
        modifier = Modifier
            .padding(start = paddings.spacingXSmall)
    )

    Box(modifier = Modifier.height(100.dp).fillMaxWidth()) {
        if (checked) {

            LicenseNumberTextField(
                licenseNumberStringValue = licenseNumberStringValue,
                onLicenseNumberTextFieldChange = onLicenseNumberTextFieldChange
            );
        }
    }
}

@Composable
fun LicenseNumberTextField(
    licenseNumberStringValue: String,
    onLicenseNumberTextFieldChange: (String) -> Unit
) {
    CustomTextField(
        textStringValue = licenseNumberStringValue,
        placeholderString = "Registration Number",
        iconImageVector = Icons.Filled.Person,
        onValueChange = onLicenseNumberTextFieldChange,
    )
}

@Composable
fun SignupButton(
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.size(paddings.spacingLarge))

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = -paddings.spacingExtraLarge/2)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = shapes.small,
            modifier = Modifier
                .padding(paddings.spacingSmall)
                .width(281.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Sign Up",
                fontWeight = FontWeight.Bold,
                style = AppTypography.bodyLarge.copy(
                    letterSpacing = 2.sp
                ) //gabaritoMediumBoldTextStyle
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun BirthdatePicker(
    birthdateString: String,
    placeholderString: String = "Birthdate",
    onValueChange: (String) -> Unit,
    iconImageVector: ImageVector = Icons.Filled.DateRange,
    onClick: () -> Unit,
    showDatePicker: Boolean,
    onDatePickerDoneClick: () -> Unit,
    onDatePickerDissmisClick: () -> Unit
) {
    OutlinedTextField(
        value = birthdateString,
        textStyle = AppTypography.bodyLarge.copy(
            color = if (birthdateString == placeholderString) {
                MaterialTheme.colorScheme.onSurface
            } else {
                Color.Black
            }
        ),
        placeholder = {
            Text(
                text = placeholderString
            )
        },
        onValueChange = onValueChange,
        shape = shapes.small,
        leadingIcon = {
            Icon(
                imageVector = iconImageVector,
                contentDescription = ""
            )
        },
        enabled = false,
        readOnly = true,
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.White,
            disabledIndicatorColor = if(showDatePicker) MaterialTheme.colorScheme.primary
                    else Color.Gray,
            disabledLeadingIconColor = Color.Black,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
            )
            .clickable {
                onClick()
            }
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
            onDatePickerDoneClick()
        },
        onDismiss = {
            onDatePickerDissmisClick()
        },
        yearsRange = 1950..LocalDate.now().year
    )
}


@Composable
fun BirthdateInputFields(
    dayValue: String,
    onDayValueChange: (String) -> Unit,
    yearValue: String,
    onYearValueChange: (String) -> Unit,
    monthValue: String,
    onMonthValueChange: (String) -> Unit,
) {
//    var day by remember { mutableStateOf("") }
//    var month by remember { mutableStateOf("") }
//    var year by remember { mutableStateOf("") }

    val currentYear = LocalDate.now().year
    val minYear = 1920

    // Create focus requesters for each TextField
    val dayFocusRequester = remember { FocusRequester() }
    val monthFocusRequester = remember { FocusRequester() }
    val yearFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current


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


//        Text(
//            text = "Birthdate",
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.onSurface,
//            modifier = Modifier
//                .offset(y = paddings.spacingXSmall)
//        )

        Text(
            text = "Enter your birthday",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall/2,
                    top = paddings.spacingXSmall
                )
                .offset(y = paddings.spacingXSmall)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between fields
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = paddings.spacingSmall)
        ) {
            // Day input
            OutlinedTextField(
                value = dayValue,
                leadingIcon = { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Date Icon") },
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        // Update day value
                        onDayValueChange(it)
                        // If day has 2 digits, move focus to the month field
                        if (it.length == 2) {
                            monthFocusRequester.requestFocus()
                        }
                    }
                },
                singleLine = true,
                label = { Text("DD") },
                isError = dayValue.isNotEmpty() && !isValidDay(dayValue), // Error state is only true when there is invalid input
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { monthFocusRequester.requestFocus() }
                ),
                modifier = Modifier
                    .width(105.dp) // Fixed width for consistency
                    .focusRequester(dayFocusRequester) // Assign focus requester for the day input
                    //.weight(1f)
            )

            // Month input
            OutlinedTextField(
                value = monthValue,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Allow up to 2 digits
                        // Update Month TODO
                        onMonthValueChange(it)
                        // If month has 2 digits, move focus to the year field
                        if (it.length == 2) {
                            yearFocusRequester.requestFocus()
                        }
                    }
                },
                label = { Text("MM") },
                isError = monthValue.isNotEmpty() && !isValidMonth(monthValue), // Error state is only true when there is invalid input
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
                    //.weight(1f)
            )


            // Year input
            OutlinedTextField(
                value = yearValue,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) { // Allow up to 4 digits
                        // Update year TODO
                        onYearValueChange(it)
                    }
                },
                label = { Text("YYYY") },
                isError = yearValue.isNotEmpty() && !isValidYear(yearValue), // Error state is only true when there is invalid input
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .width(80.dp) // Fixed width for consistency
                    .focusRequester(yearFocusRequester) // Assign focus requester for the year input
                    //.weight(1f)
            )

    }
}


@Composable
fun GenderSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {

    Text(
        text = "Select your gender",
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(
                start = paddings.spacingXSmall/2,
                top = paddings.spacingXSmall
            )
            .offset(y = paddings.spacingXSmall)
    )
    Row(
        modifier = Modifier//.fillMaxWidth()
            .width(281.dp)
            .padding(
                top = paddings.spacingXSmall,
                bottom = paddings.spacingSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val genders = listOf("Male", "Female")

        genders.forEach { gender ->
            OutlinedButton(
                onClick = { onGenderSelected(gender) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedGender == gender) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (selectedGender == gender) Color.White else Color.Black //MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurface
                ),
                shape = shapes.extraSmall,
                //border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = gender,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if(selectedGender == gender) FontWeight.Normal else FontWeight.Normal
                    )
            }
        }
    }
}



@Composable
fun GenderDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    Log.d("SIGNUP", "Gender clicked!")
                    expanded = !expanded
                }
        ) {
            CustomTextField(
                textStringValue = "",
                placeholderString = "Gender",
                iconImageVector = Icons.Filled.Person,
                onValueChange = {}
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Female") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Male") },
                onClick = { /* Do something... */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelector() {
    val countries = listOf("United States", "Canada", "Germany", "India", "Australia")
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedCountry,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Country") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(country) },
                    onClick = {
                        selectedCountry = country
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CountrySelectorWithFlags(
    onNationalityTextFieldChange: (String) -> Unit,
) {
    val countries = listOf(
        Country("Argentina", "🇦🇷"),
        Country("Australia", "🇦🇺"),
        Country("Austria", "🇦🇹"),
        Country("Belgium", "🇧🇪"),
        Country("Brazil", "🇧🇷"),
        Country("Canada", "🇨🇦"),
        Country("China", "🇨🇳"),
        Country("Czech Republic", "🇨🇿"),
        Country("Denmark", "🇩🇰"),
        Country("Egypt", "🇪🇬"),
        Country("Finland", "🇫🇮"),
        Country("France", "🇫🇷"),
        Country("Germany", "🇩🇪"),
        Country("Greece", "🇬🇷"),
        Country("India", "🇮🇳"),
        Country("Ireland", "🇮🇪"),
        Country("Israel", "🇮🇱"),
        Country("Italy", "🇮🇹"),
        Country("Japan", "🇯🇵"),
        Country("Mexico", "🇲🇽"),
        Country("Netherlands", "🇳🇱"),
        Country("New Zealand", "🇳🇿"),
        Country("Nigeria", "🇳🇬"),
        Country("Norway", "🇳🇴"),
        Country("Poland", "🇵🇱"),
        Country("Portugal", "🇵🇹"),
        Country("Romania", "🇷🇴"),
        Country("Russia", "🇷🇺"),
        Country("Saudi Arabia", "🇸🇦"),
        Country("South Africa", "🇿🇦"),
        Country("South Korea", "🇰🇷"),
        Country("Spain", "🇪🇸"),
        Country("Sweden", "🇸🇪"),
        Country("Switzerland", "🇨🇭"),
        Country("Turkey", "🇹🇷"),
        Country("UK", "🇬🇧"),
        Country("Ukraine", "🇺🇦"),
        Country("USA", "🇺🇸")
    )


    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(
                top = paddings.spacingXSmall,
                bottom = paddings.spacingXSmall
            )
            .width(281.dp)
    ) {

        Text(
            text = "Country of birth",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall/2,
                    top = paddings.spacingXSmall
                )
                .offset(y = -paddings.spacingXSmall)
        )

        OutlinedTextField(
            value = selectedCountry?.name ?: "",
            onValueChange = {},
            placeholder = { Text("Select Country") },
            shape = shapes.small,
            //leadingIcon = { Icon(imageVector = Icons.Filled.Flag) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Country Selector",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = MaterialTheme.colorScheme.primary, // Primary color for focused border
//                unfocusedBorderColor = MaterialTheme.colorScheme.secondary, // Secondary color for unfocused border
//                focusedLabelColor = MaterialTheme.colorScheme.primary, // Primary color for focused label
//                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), // Unfocused label color
//                placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // Lighter placeholder color
//                textColor = MaterialTheme.colorScheme.onSurface // Text color
//            ),
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddings.spacingXSmall)
                .clickable {
                    expanded = !expanded
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    {
                        Text(
                            text = "${country.flag} ${country.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        selectedCountry = country
                        expanded = false
                        onNationalityTextFieldChange(country.name)
                    }
                )
            }
        }
    }
}

data class Country(val name: String, val flag: String)



@Preview
@Composable
fun SignupSecondScreenPreview() {
    SignupSecondScreen(
        onEmailTexFieldChange = {},
        emailStringValue = "",
        onPasswordTextFieldChange = {},
        passwordStringValue = "",
        errorMessage = false,
        onContinueClick = {},
        onBirthdateTextFieldClick = { },
        birthdateStringValue = "",
        showDatePicker = false,
        onDatePickerDoneClick = {},
        onDatePickerDissmisClick = {},
        onNationalityTextFieldChange = {},
        nationalityStringValue = "",
        onDayTextFieldChange = {},
        dayStringValue = "",
        onMonthTextFieldChange = {},
        monthStringValue = "",
        onYearTextFieldChange = {},
        yearStringValue = "",
        selectedGender = "",
        onGenderButtonClick = {},
        onLicenseNumberTextFieldChange = {},
        licenseNumberStringValue = "",
        modifier = Modifier,
    )
}
