package com.example.racebuddy.data.network


import androidx.compose.runtime.Composable
import com.example.racebuddy.data.database.Athlete
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
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

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface StravaApiService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authorizationCode: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): ResponseBody
}

data class StravaAccessTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val athlete: StravaAthlete
)

data class StravaAthlete (
    val username: String
)

object StravaApi {
    val retrofitService: StravaApiService by lazy {
        retrofit.create(StravaApiService::class.java)
    }
}


