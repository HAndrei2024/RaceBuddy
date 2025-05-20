package com.example.racebuddy.ui.v2.signup

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.CustomTextField
import com.example.racebuddy.ui.v2.common.ErrorText
import com.example.racebuddy.ui.v2.common.LoadingAnimation

@Composable
fun SignUpFirstScreen(
    onFirstNameTextFieldChange: (String) -> Unit,
    firstNameStringValue: String,
    onLastNameTextFieldChange: (String) -> Unit,
    lastNameStringValue: String,
    onEmailTexFieldChange: (String) -> Unit,
    emailStringValue: String,
    onPasswordTextFieldChange: (String) -> Unit,
    passwordStringValue: String,
    onVerifyPasswordTextFieldChange: (String) -> Unit,
    verifyPasswordStringValue: String,
    showError: Boolean,
    isLoading: Boolean,
    errorMessage: String,
    onContinueClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            LoadingAnimation()
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
                .padding(start = paddings.spacingExtraLarge)
        ) {
            SignUpText()
//            FirstNameTextField(
//                textStringValue = firstNameStringValue,
//                placeholderString = "First Name",
//                iconImageVector = Icons.Filled.Person,
//                onValueChange = onFirstNameTextFieldChange
//
//            )
//            LastNameTextField(
//                textStringValue = lastNameStringValue,
//                placeholderString = "Last Name",
//                iconImageVector = Icons.Filled.Person,
//                onValueChange = onLastNameTextFieldChange
//            )
            EmailTextField(
                textStringValue = emailStringValue,
                placeholderString = "Email",
                iconImageVector = Icons.Filled.Email,
                onValueChange = onEmailTexFieldChange
            )
            PasswordTextField(
                passwordStringValue = passwordStringValue,
                onValueChange = onPasswordTextFieldChange,
                aboveText = "Password",
                showVerification = true
            )
            PasswordTextField(
                passwordStringValue = verifyPasswordStringValue,
                onValueChange = onVerifyPasswordTextFieldChange,
                aboveText = "Rewrite Password"
            )
            //PasswordValidationText(password = passwordStringValue, paddingsIn = PaddingValues(8.dp))
//        PaswordTextField(
//            textStringValue = passwordStringValue,
//            placeholderString = "Password",
//            iconImageVector = Icons.Filled.Lock,
//            onValueChange =  onPasswordTextFieldChange
//        )

            if (showError) {
                ErrorText(
                    textString = errorMessage
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingMedium)
            )

            ContinueButton(
                onClick = onContinueClick
            )
            PageIndicator(0)
        }
    }
}

@Composable
fun PasswordTextField(
    passwordStringValue: String,
    onValueChange: (String) -> Unit = {},
    onFocusChange: () -> Unit = {},
    aboveText: String = "Password",
    showVerification: Boolean = false
) {
    var hasFocus by remember { mutableStateOf(false) }


    Text(
        text = aboveText,
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(
                start = paddings.spacingXSmall/2,
                top = paddings.spacingXSmall
            )
            .offset(y = paddings.spacingXSmall)
    )

    OutlinedTextField(
        value = passwordStringValue,
        textStyle = AppTypography.bodyLarge,
        placeholder = {
            Text(
                text = if (!hasFocus) "********" else ""
            )
        },
        onValueChange = onValueChange,
        shape = shapes.small,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = ""
            )
        },
        modifier = Modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
            )
            .onFocusChanged { focusState ->
                // If focus was lost
                if (hasFocus && !focusState.isFocused) {
                    onFocusChange()
                }
                hasFocus = focusState.isFocused
            }
    )

//    if(hasFocus && showVerification) {
//        PasswordValidationText(passwordStringValue, paddingsIn = PaddingValues(8.dp))
//    }
    if(showVerification) {
        AnimatedVisibility(
            visible = hasFocus,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            PasswordValidationText(password = passwordStringValue, paddingsIn = PaddingValues(8.dp))
        }
    }
}


@Composable
fun PasswordValidationText(password: String, paddingsIn: PaddingValues) {
    val conditions = listOf(
        "At least 8 characters" to (password.length >= 8),
        "At least one special character" to password.any { it in "!@#\$%^&*()_-+=<>?/\\[]{}|~`" },
        "At least one digit" to password.any { it.isDigit() },
        "At least one uppercase letter" to password.any { it.isUpperCase() }
    )

    Column(
        modifier = Modifier.padding(start = paddings.spacingXSmall/2, bottom = paddings.spacingSmall)
    ) {
        conditions.forEach { (text, isValid) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        start = paddingsIn.calculateStartPadding(LayoutDirection.Ltr) / 2,
                        //top = paddingsIn.calculateTopPadding()
                    )
                    .offset(y = paddingsIn.calculateTopPadding())
            ) {
                Icon(
                    imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336), // Green / Red
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}


@Composable
fun ContinueButton(
    onClick: () -> Unit = {}
) {
    Spacer(modifier = Modifier.size(paddings.spacingSmall))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddings.spacingSmall)
            .offset(paddings.spacingExtraLarge)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = shapes.small,
            modifier = Modifier
                .padding(paddings.spacingSmall)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Continue",
                    fontWeight = FontWeight.Bold,
                    style = AppTypography.bodyLarge //gabaritoMediumBoldTextStyle
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Arrow",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun SignUpText() {
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
            text = "Connect to your new race companion!",
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
fun LastNameTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        textStringValue = textStringValue,
        placeholderString = placeholderString,
        iconImageVector = iconImageVector,
        onValueChange = onValueChange,
        modifier = Modifier
    )
}

@Composable
fun EmailTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit
) {
    Text(
        text = "Email",
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(
                start = paddings.spacingXSmall/2,
                top = paddings.spacingXSmall
            )
            .offset(y = paddings.spacingXSmall)
    )
    CustomTextField(
        textStringValue = textStringValue,
        placeholderString = placeholderString,
        iconImageVector = iconImageVector,
        onValueChange = onValueChange,
        onFocusChanged = {
            Log.d("SIGNUP1", "Verifying email... (via API)")
        }
    )
}

//@Composable
//fun PaswordTextField(
//    textStringValue: String,
//    placeholderString: String,
//    iconImageVector: ImageVector,
//    onValueChange: (String) -> Unit
//) {
//    CustomTextField(
//        textStringValue = textStringValue,
//        placeholderString = placeholderString,
//        iconImageVector = iconImageVector,
//        onValueChange = onValueChange
//    )
//}

@Composable
fun PageIndicator(currentPage: Int) {
    Spacer(
        modifier = Modifier.height(height = paddings.spacingExtraLarge)
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = paddings.spacingSmall, alignment =  Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddings.spacingMedium)
            .offset(x = -paddings.spacingExtraLarge/2)
    ) {
        repeat(2) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}



@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpFirstScreen(
        onContinueClick = {},
        onEmailTexFieldChange = {},
        emailStringValue = "Enter your email...",
        onFirstNameTextFieldChange = {},
        onLastNameTextFieldChange = {},
        onPasswordTextFieldChange = {},
        passwordStringValue = "Password",
        errorMessage = "",
        firstNameStringValue = "",
        lastNameStringValue = "",
        showError = false,
        isLoading = false,
        onVerifyPasswordTextFieldChange = {},
        verifyPasswordStringValue = "",
        modifier = TODO()
    )
}