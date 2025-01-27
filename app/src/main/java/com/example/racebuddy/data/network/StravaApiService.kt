package com.example.racebuddy.data.network


import androidx.compose.runtime.Composable
import com.example.racebuddy.data.database.Athlete
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://www.strava.com/"

private val json = Json{
    ignoreUnknownKeys = true
    coerceInputValues = true
}

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface StravaApiService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAuthDetails(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authorizationCode: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): StravaAuthResponse

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAuthDetailsJson(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authorizationCode: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): ResponseBody
}

@Serializable
data class StravaAuthResponse(
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

@Serializable
data class StravaAthlete (
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "username")
    val username: String?,
    @SerialName(value = "resource_state")
    val resourceState: Int,
    @SerialName(value = "firstname")
    val firstName: String,
    @SerialName(value = "lastname")
    val lastName: String,
    @SerialName(value = "profile")
    val profilePictureUrl: String
)

object StravaApi {
    val retrofitService: StravaApiService by lazy {
        retrofit.create(StravaApiService::class.java)
    }
}


