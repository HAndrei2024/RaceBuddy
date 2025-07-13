package com.example.racebuddy.data.database

//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.racebuddy.data.database.Result
import com.example.racebuddy.models.Organizer
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import network.chaintech.kmp_date_time_picker.utils.now
import java.io.Serial

class RemoteDataSource {

    object SupabaseClient {
        val client = createSupabaseClient(
            supabaseUrl = "https://mkiafnnklxyysprdgmcb.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1raWFmbm5rbHh5eXNwcmRnbWNiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIzNzY2MjEsImV4cCI6MjA1Nzk1MjYyMX0.PtT-E5J_KL5Geueo15TYoIax2PmomAc_9iDx75HSeGI"
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    suspend fun verifyLogin(
        email: String,
        password: String,
        isOrganizer: Boolean
    ): User {

        Log.d("LOGIN Remote Data Source", "Trying to verify login... isOrganizer = $isOrganizer" )

        try {
            val response = SupabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val id = SupabaseClient.client.auth.currentUserOrNull()?.id ?: "null"

            Log.d("LOGIN", id)

            // check if athlete or organizer

            return if(isOrganizer) {
                //check if organizer
                checkOrganizerAccountOrDefault(id)

            } else {
                checkAthleteAccountOrDefault(id)
                //return "true $id"
            }
        } catch (authException: AuthRestException) {
            return defaultAthlete
        }


    }

    suspend fun checkOrganizerAccountOrDefault(organizer_uuid: String): OrganizerInfo {
        Log.d("LOGIN Remote Data Source", "Checking if an orginzer exists for: $organizer_uuid")
        val organizer = SupabaseClient.client.from("Organizer")
            .select() {
                filter {
                    eq("organizer_uuid", organizer_uuid)
                }
            }.decodeSingleOrNull<OrganizerInfo>()

        Log.d("LOGIN Remote Data Source", "This is the response: $organizer")

        return organizer ?: defaultOrganizer
    }

    suspend fun checkAthleteAccountOrDefault(athleteUuid: String): AthleteInfo {
        val athlete = SupabaseClient.client.from("Athlete")
            .select() {
                filter {
                    eq("athlete_uuid", athleteUuid)
                }
            }.decodeSingleOrNull<AthleteInfo>()

        return athlete ?: defaultAthlete
    }

    suspend fun signUp(
        email: String,
        password: String,
        isOrganizer: Boolean
    ): String {

        try {
            val result = SupabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("is_organizer", isOrganizer)
                }
            }


            if (result == null) {
                Log.d(
                    "SIGNUP1",
                    "Result is null, getting logged in athlete..." + " From Remote Data source"
                )
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
                Log.d("Test", "${athleteInfo.birthdate}")
                set("first_name", athleteInfo.firstName)
                set("last_name", athleteInfo.lastName)
                set("gender", athleteInfo.gender)
                set("nationality", athleteInfo.country)
                set("local_registration_number", athleteInfo.licenseNumber)
            }
        ) {
            select()
            filter {
                athleteInfo.athleteId?.let { eq("athlete_uuid", it) }
            }
        }.decodeSingle<AthleteInfo>()

        val response2 = SupabaseClient.client.from("Athlete").update(
            {
                set("birthdate", athleteInfo.birthdate)
            }
        ) {
            filter {
                athleteInfo.athleteId?.let { eq("athlete_uuid", it) }
            }
        }

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

    suspend fun getOrganizerEvents(organizerUuid: String): List<EventInfo> {
        val events = SupabaseClient.client.from("Event").select() {
            filter {
                eq(column = "organizer_uuid", value = organizerUuid)
            }
            order(
                column = "created_at",
                order = Order.ASCENDING,
            )
        }.decodeList<EventInfo>()

        Log.d("Supabase Remote Data Source", "API call for organizer events: ${events.size}")

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

    suspend fun getOrganizerInfo(organizerUuid: String): OrganizerInfo {
        val organizer: OrganizerInfo = SupabaseClient.client.from("Organizer").select() {
            filter {
                eq("organizer_uuid", organizerUuid)
            }
        }
            .decodeSingle()
        Log.d("SUPABASE User", "Selected user from database: ${organizer.administratorFirstName} ${organizer.administratorLastName}")

        return organizer
    }

    suspend fun addEvent(
        title: String,
        startDate: LocalDate,
        endDate: LocalDate,
        country: String,
        city: String,
        county: String,
        details: String,
        eventCategory: String,
        athleteCategories: JsonElement,
        organizerUuid: String
    ): String {
        //Log.d("Supabase", "Adding event... ${rowData["title"]}")
        try {
            val eventInsert = EventInsert(
                title = title,
                startDate = startDate,
                endDate = endDate,
                country = country,
                city = city,
                county = county,
                details = details,
                eventCategory = eventCategory,
                categories = athleteCategories,
                organizerUuid = organizerUuid
            )



            val response = SupabaseClient.client.from("Event").insert(
                eventInsert
            ) {
                select(columns = Columns.list("event_uuid"))
            }
                .decodeSingle<EventIdResult>()

            return response.eventUuid
        } catch (exception: Exception) {
            Log.d("Supabase Add Event", "Couldn't add event: $exception")
        }
        return "-1"
    }

    suspend fun updateOrganizerDetails(
        organizerUuid: String,
        name: String,
        administratorLastName: String,
        identificationNumber: String,
        administratorFirstName: String,
        country: String,
    ): OrganizerInfo {
        try {
            val response = SupabaseClient.client.from("Organizer").update(
                {
                    set("name", name)
                    set("identification_number", identificationNumber)
                    set("administrator_first_name", administratorFirstName)
                    set("administrator_last_name", administratorLastName)
                    set("country", country)
                }
            ) {
                select()
                filter {
                    eq("organizer_uuid", organizerUuid)
                }
            }.decodeSingleOrNull<OrganizerInfo>()

            if (response != null) {
                Log.d(
                    "Supabase Organizer",
                    "Updated database succesfuly - organizer details. $response"
                )

                return response
            } else {
                Log.d("Supabase Organizer", "Database NOT updated succesfuly - organizer details.")
                return defaultOrganizer
            }
        } catch (e: Exception) {
            Log.d("Supabase Organizer", "Database NOT updated succesfuly exception: $e.")
            return defaultOrganizer
        }
    }

    suspend fun updateConfirmedFieldForRegisteredAthlete(athleteUuid: String, eventUuid: String, value: Boolean): Boolean {
        try {

            Log.d("Supabase Result Table", "Trying to update confirm column in Result table: $value")

            val response = SupabaseClient.client.from("Result").update(
                {
                    set("confirmed", value)
                }
            ) {
                filter {
                    eq("athlete_uuid", athleteUuid)
                    eq("event_uuid", eventUuid)
                }
                select(columns = Columns.list("confirmed"))
            }.decodeSingle<ResultConfirmedParam>()

            return response.confirmed
        } catch (exception: Exception) {
            Log.d("Supabase Result Table", "Couldn't update confirmed column in Result table: $exception")
            return false
        }
    }

    suspend fun getEventsInfoWithNumberOfParticipants(
        eventUuid: String,
        category: String,
        country: String
    ): List<EventInfoWithNumberOfParticipants> {
        try {

            val countryParam = CountryParam(country)
            val categoryParam = CategoryParam(category)
            val response = SupabaseClient.client.postgrest
                .rpc(
                    function = "get_events_with_participant_count",
                    parameters = mapOf(
                        "event_uuid_input" to eventUuid,
                        "country_input" to country,
                        "category_input" to category
                    )
                ).decodeList<EventInfoWithNumberOfParticipants>()

            Log.d("SUPABASE", "Fetched EventInfoWithNumberOfParticipants info: ${response.size}")

            return response
        } catch (exception: Exception) {
            Log.d("SUPABASE", "Tried fetching EventInfoWithNumberOfParticipants join data and failed, exception: $exception")
        }

        return emptyList()
    }

}

sealed class User

@Serializable
data class EventInfoWithNumberOfParticipants(
    @SerialName("title") val title: String,
    @SerialName("start_date") val startDate: LocalDate,
    @SerialName("end_date") val endDate: LocalDate,
    @SerialName("county") val county: String,
    @SerialName("city") val city: String,
    @SerialName("category") val category: String,
    @SerialName("background_picture_url") val backgroundPictureUrl: String,
    @SerialName("participant_count") val numberOfParticipants: Int
)

@Serializable
data class CategoryParam(
    @SerialName("category_input") val category: String
)

@Serializable
data class CountryParam(
    @SerialName("country_input") val country: String
)

@Serializable
data class ResultConfirmedParam(
    @SerialName("confirmed") val confirmed: Boolean
)

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
): User()

@Serializable
data class OrganizerInfo(
    @SerialName("organizer_uuid") val organizerUuid: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("name") val name: String,
    @SerialName("identification_number") val identificationNumber: String,
    @SerialName("administrator_first_name") val administratorFirstName: String,
    @SerialName("administrator_last_name") val administratorLastName: String,
    @SerialName("country") val country: String,
    @SerialName("administrator_phone_number") val administratorPhoneNumber: String,
): User()

val defaultOrganizer = OrganizerInfo(
    createdAt = "",
    organizerUuid = "-1",
    name = "",
    identificationNumber = "",
    administratorFirstName = "",
    administratorLastName = "",
    country = "",
    administratorPhoneNumber = ""
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
    @SerialName("organizer_uuid") val organizerId: String,
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
    firstName = "Cyclist",
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

val defaultAthlete: AthleteInfo = AthleteInfo(
    createdAt = "",
    firstName = "",
    lastName = "",
    birthdate = LocalDate.now(),
    gender = "",
    country = "",
    phoneNumber = "",
    username = "",
    athleteId = "-1",
    licenseNumber = "",
    uciLicenseNumber = "",
    profilePictureUrl = ""
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


@Serializable
data class AthleteCategory(
    @SerialName("max_age") val maxAge: Int,
    @SerialName("min_age") val minAge: Int,
    @SerialName("category") val category: String
)



@Serializable
data class EventInsert(
    val title: String,

    @SerialName("start_date")
    val startDate: LocalDate,

    @SerialName("end_date")
    val endDate: LocalDate,

    val country: String,
    val city: String,
    val county: String,
    val details: String,

    @SerialName("category")
    val eventCategory: String,

    // This should be a JsonArray of objects like:
    // [{"category":"Junior","min_age":10,"max_age":18}, ...]
    val categories: JsonElement,

    @SerialName("organizer_uuid")
    val organizerUuid: String
)

@Serializable
data class EventIdResult(
    @SerialName("event_uuid") val eventUuid: String
)

@Serializable
data class OrganizerDetails(
    @SerialName("name") val name: String,
    @SerialName("identification_number") val identificationNumber: String,
    @SerialName("administrator_first_name") val administratorFirstName: String,
    @SerialName("administrator_last_name") val administratorLastName: String,
    @SerialName("country") val country: String,
)
