package com.example.racebuddy.data.database

//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.racebuddy.data.database.Result
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
import io.github.jan.supabase.postgrest.rpc
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
        //Log.d("Supabase", "Getting events... - ${events}")
        return events
    }

    suspend fun getAthleteInfo(athleteId: String): AthleteInfo {
        val athlete: AthleteInfo = SupabaseClient.client.from("Athlete").select() {
            filter {
                eq("athlete_uuid", athleteId)
            }
        }
            .decodeSingle()
        Log.d("SUPABASE User", "Selected user from database: ${athlete.firstName} ${athlete.gender}")

        return athlete
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

    suspend fun getEventResultAthleteInfo(eventUuid: String): List<ResultAthleteInfo> {
        try {

            val param = EventParam(eventUuid)
            val response = SupabaseClient.client.postgrest
                .rpc("get_event_users", param).decodeList<ResultAthleteInfo>()

            Log.d("SUPABASE", "Fetched ResultAthlete info: ${response.size}")

            return response
        } catch (exception: Exception) {
            Log.d("SUPABASE", "Tried fetching ResultAthlete join data and failed, exception: $exception")
        }

        return emptyList()
    }

    suspend fun getEventResultProfileInfo(athleteUuid: String): List<EventResultProfileInfo> {

        //TODO: Check why is it getting called with athleteUuid -1
        if(athleteUuid != "-1") {
            try {
                val param = AthleteParam(athleteUuid)
                val response = SupabaseClient.client.postgrest
                    .rpc("get_athlete_results", param).decodeList<EventResultProfileInfo>()

                Log.d("SUPABASE", "Fetched EventResultProfileInfo info: ${response.size}")

                return response

            } catch (exception: Exception) {
                Log.d(
                    "SUPABASE",
                    "Tried fetching EventResultProfileInfo join data and failed, exception: $exception"
                )
            }
        }

    return emptyList()
    }


    suspend fun registerAthleteToAnEvent(athleteUuid: String, eventUuid: String, category: String) {
        Log.d("Supabase", "Adding result... athlete: $athleteUuid , event: $eventUuid")
        try {
            SupabaseClient.client.from("Result")
                .insert(
                    ResultInfo(
                        time = 0,
                        penalties = "",
                        rank = 0,
                        points = 0,
                        athleteEventNumber = "0",
                        split1 = 0,
                        split2 = 0,
                        split3 = 0,
                        split4 = 0,
                        status = "",
                        confirmed = false,
                        athleteUuid = athleteUuid,
                        eventUuid = eventUuid,
                        category = category
                    )
                )
        } catch (exception: Exception) {
            Log.d("Result Table", "Couldn't add result: $exception")
        }
    }

    suspend fun getAthleteRegisteredEvents(athleteUuid: String): List<EventInfo> {
        if (athleteUuid != "-1") {
            try {
                val param = AthleteParam(athleteUuid)
                val response = SupabaseClient.client.postgrest
                    .rpc("get_athlete_registered_events", param).decodeList<EventInfo>()

                Log.d("SUPABASE", "Fetched EventInfo (Registered) info: ${response.size}")

                return response

            } catch (exception: Exception) {
                Log.d(
                    "SUPABASE",
                    "Tried fetching EventInfo (Registered)  data and failed, exception: $exception"
                )
            }
        }

        return emptyList()
    }

    suspend fun getAthleteRegisteredEventsUuid(athleteUuid: String): List<String> {
        if(athleteUuid != "-1") {
            try {
                val param = AthleteParam(athleteUuid)
                val response = SupabaseClient.client.postgrest
                    .rpc("get_athlete_registered_events_uuid", param)
                    .decodeList<EventParamProfile>()

                Log.d(
                    "SUPABASE",
                    "Fetched EventInfo - only uuid (Registered) info: ${response.size}"
                )

                return response.map { it.eventUuid }

            } catch (exception: Exception) {
                Log.d(
                    "SUPABASE",
                    "Tried fetching EventInfo - only uuid (Registered)  data and failed, exception: $exception"
                )
            }
        }

        return emptyList()
    }

    suspend fun updateAthleteProfilePic(athleteUuid: String, profilePictureUrl: String): Boolean {
        val response = SupabaseClient.client.from("Athlete").update(
            {
                set("profile_picture_url", profilePictureUrl)
            }
        ) {
            select()
            filter {
                eq("athlete_uuid", athleteUuid)
            }
        }.decodeSingle<AthleteInfo>()

        if (response != null) {
            Log.d("Supabase Profile Screen", "Updated database succesfuly - profile pic. $response")

            return true
        } else {
            Log.d("Supabase Profile Screen", "Database NOT updated succesfuly - profile pic.")
            return false
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
data class EventParam(
    @SerialName("given_event_uuid") val eventUuid: String
)

@Serializable
data class EventParamProfile(
    @SerialName("event_uuid") val eventUuid: String
)

@Serializable
data class AthleteParam(
    @SerialName("given_athlete_uuid") val athleteUuid: String
)

@Serializable
data class AthleteInfo(
    @SerialName("created_at") val createdAt: String?,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("birthdate") val birthdate: LocalDate,
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
    @SerialName("event_uuid") val eventUuid: String,
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
    @SerialName("background_picture_url") val backgroundPictureUrl: String,
    @SerialName("categories") val categories: List<CategoriesData>
)

@Serializable
data class CategoriesData(
    @SerialName("category") val category: String,
    @SerialName("min_age") val minAge: Int,
    @SerialName("max_age") val maxAge: Int
)

@Serializable
data class ResultInfo(
    @SerialName("time") val time: Long,
    @SerialName("penalties") val penalties : String,
    @SerialName("rank") val rank: Int,
    @SerialName("points") val points: Int,
    @SerialName("athlete_event_number") val athleteEventNumber: String,
    @SerialName("s1") val split1: Long,
    @SerialName("s2") val split2: Long,
    @SerialName("s3") val split3: Long,
    @SerialName("s4") val split4: Long,
    @SerialName("status") val status: String,
    @SerialName("confirmed") val confirmed: Boolean,
    @SerialName("athlete_uuid") val athleteUuid: String,
    @SerialName("event_uuid") val eventUuid: String,
    @SerialName("category") val category: String,
)

// For the join of tables
@Serializable
data class ResultAthleteInfo(
    @SerialName("athlete_uuid") val athleteUuid: String,
    @SerialName("status") val status: String,
    @SerialName("confirmed") val confirmed: Boolean,
    @SerialName("category") val category: String,
    @SerialName("athlete_event_number") val athleteEventNumber: String,
    @SerialName("points") val points: Int,
    @SerialName("rank") val rank: Int,
    @SerialName("penalties") val penalties : String,
    @SerialName("athlete_time") val time: Long,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("gender") val gender: String,
    @SerialName("nationality") val country: String,
    @SerialName("profile_picture_url") val profilePictureUrl: String?
)

val testResultAthleteInfo = ResultAthleteInfo(
    athleteUuid = "123-123",
    status = "",
    confirmed = false,
    category = "Junior",
    athleteEventNumber = "01",
    points = 180,
    rank = 1,
    penalties = "-",
    time = 8300,
    firstName = "Jhon",
    lastName = "Doe",
    gender = "Male",
    country = "Romania",
    profilePictureUrl = ""
)

// EventInfo -> Event background, event title, event uuid,
// ResultInfo -> rank, time, category

@Serializable
data class EventResultProfileInfo(
    @SerialName("event_uuid") val evenUuid: String,
    @SerialName("title") val title: String,
    @SerialName("event_category") val eventCategory: String, //Event Category
    @SerialName("background_picture_url") val backgroundPictureUrl: String,

    @SerialName("athlete_time") val time: Long,
    @SerialName("rank") val rank: Int,
    @SerialName("athlete_event_number") val athleteEventNumber: String,
    @SerialName("result_category") val resultCategory: String, //Result Category
    @SerialName("start_date") val startDate: LocalDate

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

val testResult: ResultInfo = ResultInfo(
    time = 83000,
    penalties = "-",
    rank = 1,
    points = 75,
    athleteEventNumber = "70",
    split1 = 0,
    split2 = 0,
    split3 = 0,
    split4 = 0,
    status = "",
    confirmed = false,
    athleteUuid = "id 123 - 123",
    eventUuid = "1",
    category = "Junior"
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
    eventUuid = "1",
    backgroundPictureUrl = "",
    categories = listOf(CategoriesData("Junior", 14, 18), CategoriesData("Elite", 19, 29))
)

val testAthlete: AthleteInfo = AthleteInfo(
    createdAt = "Today",
    firstName = "Test",
    lastName = "Last",
    birthdate = LocalDate.now(),
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
    birthdate = LocalDate.now(),
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
        eventUuid = "2",
        backgroundPictureUrl = "",
        categories = listOf(CategoriesData("Junior", 14, 18), CategoriesData("Elite", 19, 29))
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
        eventUuid = "3",
        backgroundPictureUrl = "",
        categories = listOf(CategoriesData("Junior", 14, 18), CategoriesData("Elite", 19, 29))
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
        eventUuid = "4",
        backgroundPictureUrl = "",
        categories = listOf(CategoriesData("Junior", 14, 18), CategoriesData("Elite", 19, 29))
    )
)