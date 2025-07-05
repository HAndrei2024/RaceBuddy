package com.example.racebuddy.ui.v2.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import kotlinx.coroutines.delay

@Composable
fun ConfirmationScreen(
    onContinueClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var showCheck by remember { mutableStateOf(false) }

    // Simulate loading
    LaunchedEffect(Unit) {
        delay(500) // loading duration
        isLoading = false
        showCheck = true
        delay(500) // check visible for 1 second
        // Optionally do something else after

    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }

            // Check icon
            AnimatedVisibility(visible = showCheck) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingExtraLarge)
            )

            Button(
                onClick = onContinueClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = shapes.small,
                modifier = Modifier
                    //.padding(paddings.spacingSmall)
                    .width(281.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Continue!",
                    fontWeight = FontWeight.Bold,
                    style = AppTypography.bodyLarge.copy(
                        letterSpacing = 2.sp
                    ) //gabaritoMediumBoldTextStyle
                )
                //Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun ConfirmationScreenPreview() {
    ConfirmationScreen(
        onContinueClick = {}
    )
}