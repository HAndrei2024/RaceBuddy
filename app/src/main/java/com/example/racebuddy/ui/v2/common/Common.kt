package com.example.racebuddy.ui.v2.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes

@Composable
fun CustomTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
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
        leadingIcon = {
            Icon(
                imageVector = iconImageVector,
                contentDescription = ""
            )
        },
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

    )

}

@Composable
fun ErrorText(
    textString: String = "Incorrect email or password."
) {
    Text(
        text = textString,
        style = AppTypography.bodyLarge,
        color = Color.Red
    )
}

@Composable
fun LoadingAnimation() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigationClick: () -> Unit,
    showBackNavigation: Boolean
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            if(showBackNavigation) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // or Icons.Default.Menu
                        contentDescription = "Back"
                    )
                }
            }
    },
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
    )


    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        onNavigationClick = {},
        showBackNavigation = false
    )
}