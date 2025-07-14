package com.example.racebuddy.ui.v2.organizer.updateResults

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.network.LocalServerApi
import com.example.racebuddy.ui.v2.organizer.updateDetails.OrganizerUpdateDetailsScreenViewModel
import com.example.racebuddy.ui.v2.organizer.updateDetails.OrganizerUpdateDetailsUiState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.nio.file.FileAlreadyExistsException
import okhttp3.MultipartBody.Companion as MultipartBody1

class OrganizerUpdateResultsScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrganizerUpdateResultsUiState(
            fileName = "",
            fileUri = Uri.EMPTY
        )
    )
    val uiState = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState
        .map { state ->
            listOf(
                state.fileName,
                state.fileUri
            ).all { it != "" }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun updateSelectedFileUri(fileUri: Uri) {
        _uiState.update { currentValue ->
            var fileName: String = ""
            if(fileUri != Uri.EMPTY) {
                val index = fileUri.path?.lastIndexOf("/") ?: 0

                fileName = fileUri.path?.substring(index + 1) ?: ""

            }
            currentValue.copy(
                fileUri = fileUri,
                fileName = fileName
            )
        }
    }

    fun uploadExcelToServer(
        context: Context,
        uploadUrl: String = "http://127.0.0.1:5000/upload",
        eventUuid: String
    ) {
        updateIsSuccessful(false)
        updateResponseMessage("")

        val columns = mapOf(
            "name" to "name",
            "time" to "time",
            "penalties" to "penalties",
            "rank" to "rank",
            "points" to "points",
            "athlete_event_number" to "athlete_event_number",
            "s1" to "s1",
            "s2" to "s2",
            "s3" to "s3",
            "s4" to "s4",
            "status" to "status",
            "confirmed" to "confirmed",
            "category" to "category"
        )

        val gson = Gson()

        val data = mutableMapOf<String, Any>(
            "columns" to gson.toJson(columns),
            "event_id" to "13c94ae9-ae3a-4b50-81e4-96d3cf7bc319"
        )

        //val path = _uiState.value.fileUri.path ?: ""
        //val file = File(path)

// Create file part
        val fileUri = _uiState.value.fileUri ?: return
        val tempFile = copyUriToTempFile(context, fileUri) ?: return

        val fileRequestBody = tempFile.asRequestBody(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".toMediaType()
        )
        val filePart = MultipartBody.Part.createFormData("file", tempFile.name, fileRequestBody)

        val columnsJson = Gson().toJson(columns)
        val columnsPart = columnsJson.toRequestBody("text/plain".toMediaType())
        val eventUuidPart = eventUuid.toRequestBody("text/plain".toMediaType())

        viewModelScope.launch {
            try {
                updateIsLoading(true)

                val response = LocalServerApi.retrofitService.uploadExcel(
                    file = filePart,
                    columns = columnsPart,
                    eventId = eventUuidPart
                )

                if (response.isSuccessful) {
                    Log.d("UPLOAD", "Success with status code: ${response.code()}")
                    updateIsLoading(false)
                    updateResponseMessage("File uploaded and results updated.")
                    updateIsSuccessful(true)
                } else {
                    Log.e("UPLOAD", "Error: HTTP ${response.code()}")

                    updateIsLoading(false)
                    updateIsSuccessful(false)
                    updateResponseMessage("Request error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Exception: ${e.localizedMessage}")

                updateIsLoading(false)
                updateIsSuccessful(false)
                updateResponseMessage("Exception error: ${e.localizedMessage}")

            }
        }
        

//        val client = OkHttpClient()
//        val file = _uiState.value.fileUri.path?.let { File(it) }
//        val requestBody = file?.let {
//            MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", file.name, it.asRequestBody("application/octet-stream".toMediaTypeOrNull()))
//                .addFormDataPart("columns", gson.toJson(columns))
//                .addFormDataPart("event_id", eventUuid)
//                .build()
//        }
//
//        val request = requestBody?.let {
//            Request.Builder()
//                .url(uploadUrl)
//                .post(it)
//                .build()
//        }
//
//        if (request != null) {
//            val response = client.newCall(request).execute()
//
//            Log.d("OrganizerUpdateResults", "Request made, result: $response")
//        }
    }

    fun copyUriToTempFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val fileName = getFileName(context, uri) ?: "temp.xlsx"
        val tempFile = File(context.cacheDir, fileName)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    fun updateIsLoading(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = value
            )
        }
    }

    fun updateIsSuccessful(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isSuccessful = value
            )
        }
    }

    fun updateResponseMessage(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                responseMessage = message
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                OrganizerUpdateResultsScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class OrganizerUpdateResultsUiState(
    val fileName: String,
    val fileUri: Uri,
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val responseMessage: String = ""
)