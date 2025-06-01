package com.example.racebuddy.data.database

//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
import android.util.Log
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.chaintech.kmp_date_time_picker.utils.now

class RemoteDataSource {

    suspend fun verifyLogin(
        email: String,
        password: String
    ): String {

        try {
            val response = SupabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val id = SupabaseClient.client.auth.currentUserOrNull()?.id ?: "null"

            Log.d("LOGIN", id)

            return "true $id"
        } catch (authException: AuthRestException) {
            return authException.message.toString()
        }


    }

    suspend fun signUp(
        email: String,
        password: String
    ): String {

        try {
            val result = SupabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            if(result == null) {
                Log.d("SIGNUP1", "Result is null, getting logged in athlete..." + " From Remote Data source")
                return "true " + getLoggedInAthlete()
            }

            Log.d("SIGNUP1", result.toString() + " From Remote Data source")

            return "true " + result?.id
        } catch (e: Exception) {
            Log.e("SignUp", "Error: ${e.message}")
            if(e.message?.contains("user_already_exists") == true) {
                Log.e("SignUp", "Email already used.")

                return "Email already used."
            }
            if (e.message?.contains("validation_failed") == true) {
                Log.e("SignUp", "Email invalid.")

                return "Invalid email."
            } else if (e.message?.contains("password") == true) {


                return "Password doesn't meet requirments."
            }
            else {
                return "Something went wrong."
            }
        }

    }

    suspend fun updateUserDetails(
        athleteInfo: AthleteInfo
    ): Boolean {
        val response = SupabaseClient.client.from("Athlete").update(
            {
                //or
                set("first_name", athleteInfo.firstName)
                set("last_name", athleteInfo.lastName)
                set("gender", athleteInfo.gender)
                set("birthdate", athleteInfo.birthdate)
                set("nationality", athleteInfo.country)
                set("local_registration_number", athleteInfo.licenseNumber)
            }
        ) {
            select()
            filter {
                athleteInfo.athleteId?.let { eq("athlete_uuid", it) }
            }
        }.decodeSingle<AthleteInfo>()

        if (response != null) {
            Log.d("SIGNUP2", "Updated database succesfuly.")

            return true
        } else {
            Log.d("SIGNUP2", "Database NOT updated succesfuly.")
            return false
        }
    }

    fun getLoggedInAthlete(): String {
        return SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""
    }

    suspend fun getEvents(): List<EventInfo> {

        val events =  SupabaseClient.client.from("Event").select(){
//            filter {
//                gt(
//                    column = "start_date",
//                    value = LocalDate.now()
//                )
//            }
            
            order(
                column = "created_at",
                order = Order.ASCENDING,
            )
        }.decodeList<EventInfo>()
        Log.d("Supabase", "Getting events... - ${events}")
        return events
    }

    suspend fun getAthleteInfo(athleteId: String): AthleteInfo {
        return SupabaseClient.client.from("Athlete").select() {
            filter {
                eq("athlete_uuid", athleteId)
            }
        }
            .decodeSingle()
    }

    suspend fun getFavoriteEvents(athleteId: String): List<EventIdForFavorite> {
        val eventIds =  SupabaseClient.client.from("Favorites")
            .select(columns = Columns.list("event_uuid")) {
            filter {
                eq("athlete_uuid", athleteId)
            }
        }.decodeList<EventIdForFavorite>()

        Log.d("SUPABASE", "Favorite events: $eventIds")

        return eventIds
    }

    suspend fun addFavoriteEvent(athleteUuid: String, eventUuid: String) {

        Log.d("Supabase", "Adding favorite event... $athleteUuid , $eventUuid")
        try {
            SupabaseClient.client.from("Favorites").insert(Favorites(athleteUuid, eventUuid))
        } catch (exception: Exception) {
            Log.d("Favorites Table", "Couldn't add favorite event: $exception")
        }
    }

    suspend fun deleteFavoriteEvent(athleteUuid: String, eventUuid: String) {
        try {
            SupabaseClient.client.from("Favorites").delete() {
                filter {
                    eq("athlete_uuid", athleteUuid)
                    eq("event_uuid", eventUuid)
                }
            }
        } catch (exception: Exception) {
            Log.d("Favorites Table", "Couldn't delete favorite event: $exception")
        }
    }

    suspend fun logoutAthlete() {
        try {
            SupabaseClient.client.auth.signOut()
        } catch (exception: Exception) {
            Log.d("SUPABASE", "Tried to logged out athlete, exception: $exception")
        }
    }

    object SupabaseClient {
        val client = createSupabaseClient(
            supabaseUrl = "https://mkiafnnklxyysprdgmcb.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1raWFmbm5rbHh5eXNwcmRnbWNiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIzNzY2MjEsImV4cCI6MjA1Nzk1MjYyMX0.PtT-E5J_KL5Geueo15TYoIax2PmomAc_9iDx75HSeGI"
        ) {
            install(Postgrest)
            install(Auth)
        }
    }
}

@Serializable
data class AthleteInfo(
    @SerialName("created_at") val createdAt: String?,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("birthdate") val birthdate: String?,
    @SerialName("gender") val gender: String?,
    @SerialName("nationality") val country: String?,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("username") val username: String?,

    // Mapping athlete_id to athleteId
    @SerialName("athlete_uuid") val athleteId: String?,
    @SerialName("local_registration_number") val licenseNumber: String?,
    @SerialName("uci_registration_number") val uciLicenseNumber: String?,
    @SerialName("profile_picture_url") val profilePictureUrl: String?
)


@Serializable
data class EventInfo(
    @SerialName("event_uuid") val evenUuid: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("title") val title: String,
    @SerialName("start_date") val startDate: LocalDate,
    @SerialName("end_date") val endDate: LocalDate,
    @SerialName("country") val country: String,
    @SerialName("county") val county: String,
    @SerialName("city") val city: String,
    @SerialName("details") val details: String,
    @SerialName("track") val track: String,
    @SerialName("category") val category: String,
    @SerialName("organizer_id") val organizerId: String,
    @SerialName("background_picture_url") val backgroundPictureUrl: String
)

@Serializable
data class EventIdForFavorite(
    @SerialName("event_uuid") val eventUuid: String
)

@Serializable
data class Favorites(
    @SerialName("athlete_uuid") val athleteUuid: String,
    @SerialName("event_uuid") val eventUuid: String,
)

val testEvent: EventInfo = EventInfo(
    title = "Campionatul National de Downhill Cheile Gradistei",
    startDate = LocalDate.now(),
    endDate = LocalDate.now().plus(1, DateTimeUnit.DAY),
    country = "Romania",
    county = "Brasov",
    city = "Cheile Gradistei",
    details = "Complexul de la Cheile Grădiștei găzduiește între 24-25 septembrie Campionatul Național de Downhill.\n\n" +
            "Traseul este cel binecunoscut, a fost curățat temeinic, cu contrapantele și săriturile refăcute. A suferit modificări și ajustări minore, are câteva elemente și linii noi.\n\n" +
            "Antrenamentele au loc vineri, calificările și finala sâmbăta. Sâmbătă seară avem la restaurantul cu autoservire de lângă Sala Sporturilor un party cu DJ, foc de tabără și o gustare din partea casei. În afară de asta, în zonă, la doi pași, se află și pumptrack-ul nostru modular, care sigur va ține lumea activată.\n\n" +
            "Cazarea se face la complexul Cheile Grădiștei Moieciu sau complexul Cheile Grădiștei Fundata: ambele au restaurant, piscină cu jacuzzi și saună și sunt situate foarte aproape de traseul de concurs.\n\n" +
            "Taxa de participare este de 200 de lei, pentru cei care se înscriu on-line, și de 250 de lei, pentru cei care se înscriu la fața locului.\n\n" +
            "Categoriile de concurs sunt: Feminin Open, Copii (12-14 ani), Juniori (15-18 ani), Elite (19-29 ani), Masters 1 (30-39 ani), Masters 2 (40-49 ani) și Hobby.",
    track = "Track",
    organizerId = "",
    category = "XC",
    createdAt = LocalDate.now().toString(),
    evenUuid = "1",
    backgroundPictureUrl = ""
)

val testAthlete: AthleteInfo = AthleteInfo(
    createdAt = "Today",
    firstName = "Test",
    lastName = "Last",
    birthdate = LocalDate.now().toString(),
    gender = "Male",
    country = "Romania",
    phoneNumber = "",
    username = "",
    athleteId = "-1",
    licenseNumber = "",
    uciLicenseNumber = "",
    profilePictureUrl = "https://mkiafnnklxyysprdgmcb.supabase.co/storage/v1/object/public/pictures//4fc5528145aac3fcd27b68038b821e4420f6f8a08725d3a2b8e19a1ccff67d51.jpg"
)

val firstAthlete: AthleteInfo = AthleteInfo(
    createdAt = "Today",
    firstName = "Test",
    lastName = "Last",
    birthdate = LocalDate.now().toString(),
    gender = "Male",
    country = "Romania",
    phoneNumber = "",
    username = "",
    athleteId = "-2",
    licenseNumber = "",
    uciLicenseNumber = "",
    profilePictureUrl = "https://mkiafnnklxyysprdgmcb.supabase.co/storage/v1/object/public/pictures//4fc5528145aac3fcd27b68038b821e4420f6f8a08725d3a2b8e19a1ccff67d51.jpg"
)

val cyclingEvents = listOf(
    EventInfo(
        title = "Tour Down Under",
        startDate = LocalDate(2025, 1, 14),
        endDate = LocalDate(2025, 1, 21),
        country = "Australia",
        county = "South Australia",
        city = "Adelaide",
        details = "The Tour Down Under is the opening event of the UCI World Tour and features top-tier international cyclists.",
        track = "Urban and countryside roads",
        category = "Stage Race",
        organizerId = "org001",
        createdAt = LocalDate.now().toString(),
        evenUuid = "2",
        backgroundPictureUrl = ""
    ),
    EventInfo(
        title = "Amgen Tour of California",
        startDate = LocalDate(2025, 5, 12),
        endDate = LocalDate(2025, 5, 18),
        country = "USA",
        county = "Various",
        city = "Sacramento",
        details = "One of the largest cycling events in the United States, covering diverse Californian terrain.",
        track = "Mountain and coastal roads",
        category = "Stage Race",
        organizerId = "org002",
        createdAt = LocalDate.now().toString(),
        evenUuid = "3",
        backgroundPictureUrl = ""
    ),
    EventInfo(
        title = "Tour de Pologne",
        startDate = LocalDate(2025, 8, 3),
        endDate = LocalDate(2025, 8, 9),
        country = "Poland",
        county = "Various",
        city = "Kraków",
        details = "An important European stage race that is part of the UCI World Tour.",
        track = "Hilly terrain with city finishes",
        category = "Stage Race",
        organizerId = "org003",
        createdAt = LocalDate.now().toString(),
        evenUuid = "4",
        backgroundPictureUrl = ""
    )
)