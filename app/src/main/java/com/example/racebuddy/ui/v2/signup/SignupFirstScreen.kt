package com.example.racebuddy.ui.v2.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.CustomTextField
import com.example.racebuddy.ui.v2.login.LoginButton
import com.example.racebuddy.ui.v2.login.PasswordTextField

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
    errorMessage: Boolean,
    onContinueClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(start = paddings.spacingExtraLarge)
    ) {
        SignUpText()
        FirstNameTextField(
            textStringValue = firstNameStringValue,
            placeholderString = "First Name",
            iconImageVector = Icons.Filled.Person,
            onValueChange = onFirstNameTextFieldChange

        )
        LastNameTextField(
            textStringValue = lastNameStringValue,
            placeholderString = "Last Name",
            iconImageVector = Icons.Filled.Person,
            onValueChange = onLastNameTextFieldChange
        )
        EmailTextField(
            textStringValue = emailStringValue,
            placeholderString = "Email",
            iconImageVector = Icons.Filled.Email,
            onValueChange = onEmailTexFieldChange
        )
        PasswordTextField(
            passwordStringValue = passwordStringValue,
            onValueChange = onPasswordTextFieldChange
        )
//        PaswordTextField(
//            textStringValue = passwordStringValue,
//            placeholderString = "Password",
//            iconImageVector = Icons.Filled.Lock,
//            onValueChange =  onPasswordTextFieldChange
//        )

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
fun FirstNameTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit
) {
    CustomTextField(
        textStringValue = textStringValue,
        placeholderString = placeholderString,
        iconImageVector = iconImageVector,
        onValueChange = onValueChange
    )
}

@Composable
fun LastNameTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit
) {
    CustomTextField(
        textStringValue = textStringValue,
        placeholderString = placeholderString,
        iconImageVector = iconImageVector,
        onValueChange = onValueChange
    )
}

@Composable
fun EmailTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit
) {
    CustomTextField(
        textStringValue = textStringValue,
        placeholderString = placeholderString,
        iconImageVector = iconImageVector,
        onValueChange = onValueChange
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
        errorMessage = false,
        firstNameStringValue = "",
        lastNameStringValue = "",
    )
}