package com.example.racebuddy.ui.v2.organizer.updateResults

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.LoadingAnimation
import com.example.racebuddy.ui.v2.organizer.addEvent.BottomBarWithButtonUsingShadow
import com.example.racebuddy.ui.v2.organizer.addEvent.CustomTopAppBarMaterial3

@Composable
fun OrganizerUpdateResultsScreen(
    selectedFileName: String,
    onUpdateClick: () -> Unit,
    isUpdateButtonEnabled: Boolean,
    showError: Boolean,
    errorMessage: String,
    isLoading: Boolean,
    isSuccessful: Boolean,
    onBackClick: () -> Unit,
    setSelectedFile: (uri: Uri) -> Unit,
    modifier: Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBarMaterial3(
                title = "Update Details",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarWithButtonUsingShadow(
                buttonText = "Update!",
                onClick = onUpdateClick,
                isEnabled = isUpdateButtonEnabled,
                showError = showError,
                leadingIcon = if(isSuccessful) "✅" else "❌",
                textColor = Color.Black,
                errorMessage = errorMessage
            )
        },
        containerColor = Color.White,
        contentColor = Color.Gray
    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if(isLoading) {
                LoadingAnimation()
            }

            ExcelUploadScreen(
                selectedFileName = selectedFileName,
                setSelectedFile = setSelectedFile
            )
        }
    }
}


@Composable
fun ExcelUploadScreen(
    selectedFileName: String,
    setSelectedFile: (uri: Uri) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let { // set selected file
                setSelectedFile(it)
                Log.d("Update results UI", "$uri")
            }
        }
    )
    Button(
        onClick = {
            launcher.launch(arrayOf(
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ))

        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary ,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(2.dp),
        shape = RoundedCornerShape(shapes.small.topEnd),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Attach Excel File")
    }

    Text("Attached: $selectedFileName", color = Color.Gray)

}

@Preview
@Composable
fun OrganizerUpdateResultsScreenPreview() {
    OrganizerUpdateResultsScreen(
        onUpdateClick = {  },
        isUpdateButtonEnabled = false,
        showError = false,
        errorMessage = "",
        onBackClick = {},
        modifier = Modifier,
        setSelectedFile = {},
        selectedFileName = "",
        isLoading = false,
        isSuccessful = false
    )
}
