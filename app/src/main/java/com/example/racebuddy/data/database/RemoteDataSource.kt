package com.example.racebuddy.data.database

//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
import android.util.Log
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class RemoteDataSource {

    suspend fun verifyLogin(
        email: String,
        password: String): String {
        try {
            SupabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (authException: AuthRestException) {
            return authException.message.toString()
        }

        return "true"
    }

}

object SupabaseClient{
    val client = createSupabaseClient(
        supabaseUrl = "https://mkiafnnklxyysprdgmcb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1raWFmbm5rbHh5eXNwcmRnbWNiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIzNzY2MjEsImV4cCI6MjA1Nzk1MjYyMX0.PtT-E5J_KL5Geueo15TYoIax2PmomAc_9iDx75HSeGI"
    ) {
        install(Postgrest)
        install(Auth)
    }
}

@Serializable
data class AthleteInfo(
    @SerialName("created_at") val createdAt: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("birthdate") val birthdate: String,
    @SerialName("gender") val gender: String,
    @SerialName("country") val country: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("username") val username: String,

    // Mapping athlete_id to athleteId
    @SerialName("athlete_uuid") val athleteId: String
)
