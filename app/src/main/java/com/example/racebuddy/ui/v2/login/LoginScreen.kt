package com.example.racebuddy.ui.v2.login

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.racebuddy.R
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.Spacing
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.LoadingAnimation

@Composable
fun LoginScreen(
    onEmailTexFieldChange: (String) -> Unit,
    emailStringValue: String,
    onPasswordTextFieldChange: (String) -> Unit,
    passwordStringValue: String,
    errorMessage: Boolean,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    onSignUpClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedBlur by animateDpAsState(targetValue = if(isLoading) 1.dp else 0.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            LoadingAnimation()
            //RotatingLoader()
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxSize()
                .padding(start = paddings.spacingExtraLarge)
                .blur(
                    radius = animatedBlur,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
        ) {
            LoginText(
                onSignUpClick = onSignUpClick
            )
            EmailTextField(
                emailStringValue = emailStringValue,
                onValueChange = onEmailTexFieldChange
            )
            PasswordTextField(
                passwordStringValue = passwordStringValue,
                onValueChange = onPasswordTextFieldChange,
                onFocusChange = {}
            )

            if (errorMessage) {
                ErrorText()
            }

            LoginButton(
                onClick = onLoginClick
            )
            SkipText(
                onClick = onSkipClick
            )
        }
    }
}



@Composable
fun LoginText(
    onSignUpClick: () -> Unit = {}
) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "Or create an account to sign up!"
        val startIndex = str.indexOf("create")
        val endIndex = str.indexOf("account") + "account".length
        append(str)

        addStyle(
            style = SpanStyle(
                color = Color.Gray,
                fontStyle = AppTypography.titleSmall.fontStyle, //gabaritoMediumBoldTextStyle
                fontSize = AppTypography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold
                ), start = 0, end = str.length
        )

        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontStyle = AppTypography.titleSmall.fontStyle, //gabaritoMediumBoldTextStyle.fontStyle,
                fontSize = AppTypography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )

        addStringAnnotation(
            tag = "SignUp",
            annotation = "Navigate to Sign Up",
            start = startIndex,
            end = endIndex)
    }

    Column(
        modifier = Modifier
            .padding(bottom = paddings.spacingExtraLarge)
    ) {
        Text(
            text = "Log In",
            style = AppTypography.displayLarge,
            modifier = Modifier
                .padding(bottom = paddings.spacingXSmall)
        )
        ClickableText(
            text = annotatedLinkString,
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("SignUp", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        //Function call or just function reference? -> onSignUpClick
                        Log.d("LOGIN", "Should go to signup")
                        onSignUpClick()
                    }
            },
        )
    }
}

@Composable
fun EmailTextField(
    emailStringValue: String,
    onValueChange: (String) -> Unit = { }
) {
    OutlinedTextField(
        value = emailStringValue,
        textStyle = AppTypography.bodyLarge.copy(
            color = if(emailStringValue == "Enter your email...") {
                MaterialTheme.colorScheme.onSurface
            } else {
                Color.Black
            }
        ),
        placeholder = {
            Text(
                text = "Enter your email..."
            )
        },
        onValueChange = onValueChange,
        shape = shapes.small,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = ""
                )
        },
        modifier = Modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
                )
    )
}

@Composable
fun PasswordTextField(
    passwordStringValue: String,
    onValueChange: (String) -> Unit = {},
    onFocusChange: () -> Unit = {}
) {
    var hasFocus by remember { mutableStateOf(false) }


    OutlinedTextField(
        value = passwordStringValue,
        textStyle = AppTypography.bodyLarge,
        placeholder = {
            Text(
                text = if(!hasFocus) "********" else "")
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
}

@Composable
fun LoginButton(
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
fun SkipLoginClickableText() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val textMeasurer = rememberTextMeasurer()
        val textToDraw = "A"
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0.dp.toPx(), y = canvasWidth / 2),
                end = Offset(x = canvasHeight/3, y = canvasWidth / 2),
                color = Color.Gray,
                strokeWidth = 2f
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "My text",
            )
        }
        Text(
            text = "My text",
            color = Color.Gray
        )
    }
}

@Composable
fun LineWithTextCanvas(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Spacer(modifier = Modifier.size(paddings.spacingExtraLarge))

    var textBounds by remember { mutableStateOf<Rect?>(null) }

    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "Skip"
        val startIndex = str.indexOf("Skip")
        val endIndex = str.indexOf("Skip") + "skip".length
        append(str)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
        modifier.clickable{
            Log.d("Skip", "Skip pressed")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    textBounds?.let {
                        if (it.contains(offset)) {
                            Log.d("tag", "Text pressed!")
                        }
                    }
                }
            }
    ) {
        Canvas(modifier = modifier.height(40.dp).fillMaxWidth()
                              .offset(x = (-paddings.spacingExtraLarge/2))        // Negates parent start padding
        ) {

            // Measure text width
            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = AppTypography.bodyMedium
            )
            val textWidth = textLayoutResult.size.width.toFloat()
            val textHeight = textLayoutResult.size.height.toFloat()

            // Compute positions
            val centerY = size.height / 2
            val centerX = size.width / 2

            val padding = 16.dp.toPx()
            val spaceAroundText = 8.dp.toPx()

            // Start and end of lines
            val lineStart = 0f + padding
            val lineEnd = size.width - padding

            val textStartX = centerX - textWidth / 2
            val textEndX = centerX + textWidth / 2

            // Draw left line
            drawLine(
                color = color,
                start = Offset(x = lineStart, y = centerY),
                end = Offset(x = textStartX - spaceAroundText, y = centerY),
                strokeWidth = 2.dp.toPx()
            )

            // Draw right line
            drawLine(
                color = color,
                start = Offset(x = textEndX + spaceAroundText, y = centerY),
                end = Offset(x = lineEnd, y = centerY),
                strokeWidth = 2.dp.toPx()
            )

            // Draw the text
            drawText(
                textMeasurer = textMeasurer,
                text = annotatedLinkString,
                topLeft = Offset(x = textStartX, y = centerY - textHeight / 2),
                style = TextStyle(color = color, fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun SkipText(
    onClick: () -> Unit = {}
) {
    Spacer(modifier = Modifier.size(paddings.spacingExtraLarge * 2))

    HorizontalDivider(
        modifier = Modifier
            .offset(x = (-paddings.spacingExtraLarge/2))
            .padding(start = paddings.spacingMedium, end = paddings.spacingMedium, bottom = paddings.spacingSmall),     // Negates parent start padding ,
        thickness = 1.dp,
        color = Color.Gray
    )

    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "Skip"
        val startIndex = str.indexOf("Skip")
        val endIndex = str.indexOf("Skip") + "skip".length
        append(str)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontStyle = AppTypography.bodyMedium.fontStyle,
                fontWeight = FontWeight.Bold
            ), start = startIndex, end = endIndex
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(x = (-paddings.spacingExtraLarge/2))       // Negates parent start padding
            .fillMaxWidth()
    ){
        ClickableText(
            text = annotatedLinkString,
            onClick = { onClick() }
        )
    }
}

@Composable
fun ErrorText() {
    Text(
        text = "Incorrect email or password.",
        style = AppTypography.bodyLarge,
        color = Color.Red
    )
}



@Composable
fun RotatingLoader() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .rotate(rotation),
        tint = Color.Blue
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = {},
        onEmailTexFieldChange = {},
        emailStringValue = "Enter your email...",
        onSkipClick = {},
        onPasswordTextFieldChange = {},
        passwordStringValue = "Password",
        onSignUpClick = {},
        errorMessage = false,
        isLoading = true
    )
}

@Preview
@Composable
fun EmailTextPreview() {
    EmailTextField("")
}