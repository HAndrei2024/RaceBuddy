package com.example.racebuddy.ui.v2.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes

@Composable
fun CustomTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit,
    supportingText:@Composable() (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

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
        supportingText = supportingText,
        modifier = Modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
            )
    )

}