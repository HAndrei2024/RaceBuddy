package com.example.racebuddy.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

private const val BASE_URL = "http://10.0.2.2:5000/"

private val json = Json{
    ignoreUnknownKeys = true
    coerceInputValues = true
}

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface LocalServerApiService {

    @Multipart
    @POST("upload")
    suspend fun uploadExcel(
        @Part file: MultipartBody.Part,
        @Part("columns") columns: RequestBody,
        @Part("event_id") eventId: RequestBody
    ): Response<Unit>

}

@Serializable
data class LocalServerUploadResponse(
    @SerialName(value = "token_type")
    val tokenType: String,
    @SerialName(value = "expires_at")
    val expiresAt: Int,
    @SerialName(value = "refresh_token")
    val refreshToken: String,
    @SerialName(value = "access_token")
    val accessToken: String,
    @SerialName(value = "athlete")
    val athlete: StravaAthlete
)

object LocalServerApi {
    val retrofitService: LocalServerApiService by lazy {
        retrofit.create(LocalServerApiService::class.java)
    }
}


