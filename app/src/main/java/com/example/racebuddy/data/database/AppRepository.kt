package com.example.racebuddy.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

class AppRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    fun getData() {
        localDataSource.getData()
    }

    suspend fun verifyLogin(
        username: String,
        password: String
    ): Int {
        return localDataSource.verifyLogin(username, password)
    }

    suspend fun verifySupabaseLogin(
        email: String,
        password: String,
        isOrganizer: Boolean
    ): User {
        return remoteDataSource.verifyLogin(email, password, isOrganizer)
    }

    suspend fun signUpSupabase(
        email: String,
        password: String,
        isOrganizer: Boolean
    ): String {
        return remoteDataSource.signUp(email, password, isOrganizer)
    }

    suspend fun createNewAccount(athlete: Athlete) {
        localDataSource.createNewAccount(athlete)
    }

    suspend fun getUsernameById(id: Int): String {
        return localDataSource.getUsernameById(id)
    }

    fun getListOfEvents(title: String): Flow<List<Event>> {
        return localDataSource.getListOfEvents(title)
    }

    fun checkFavoriteEvent(userId: Int, eventId: Int): Flow<Int> {
        return localDataSource.checkFavoriteEvent(userId, eventId)
    }

    fun getListOfFavoriteEvents(athleteId: Int): Flow<List<Event>> {
        return localDataSource.getListOfFavoriteEvents(athleteId)
    }

    suspend fun addFavoriteEvent(athleteId: Int, eventId: Int) {
        localDataSource.addFavoriteEvent(athleteId, eventId)
    }

    suspend fun removeFavoriteEvent(athleteId: Int, eventId: Int) {
        localDataSource.removeFavoriteEvent(athleteId, eventId)
    }

    fun getAthlete(id: Int): Flow<Athlete> {
        return localDataSource.getAthlete(id)
    }

    fun getEvent(id: Int): Flow<Event> {
        return localDataSource.getEvent(id)
    }

    suspend fun insertResult(result: Result) {
        localDataSource.insertResult(result)
    }

    fun getAllEventResults(eventId: Int): Flow<List<Result>> {
        return localDataSource.getAllEventResults(eventId)
    }

    suspend fun getAllAthleteResults(athleteId: Int): List<Result> {
        return localDataSource.getAllAthleteResults(athleteId)
    }

    suspend fun getAllEventResultsNotFlow(eventId: Int): List<Result> {
        return localDataSource.getAllEventResultsNotFlow(eventId)
    }

    fun checkIfAthleteRegistered(athleteId: Int, eventId: Int): Flow<Int> {
        return localDataSource.checkIfAthleteRegistered(athleteId, eventId)
    }

    suspend fun updateAthleteProfilePicture(profilePictureUrl: String, id: Int) {
        localDataSource.updateAthleteProfilePicture(
            profilePictureUrl = profilePictureUrl,
            id = id
        )
    }

    fun getSupabaseLoggedInAthlete(): String {
        return remoteDataSource.getLoggedInAthlete()
    }

    suspend fun updateSupabaseAthleteDetails(
        athleteInfo: AthleteInfo
    ): Boolean {
        return remoteDataSource.updateUserDetails(athleteInfo)
    }

    suspend fun getSupabaseEvents(): List<EventInfo> {
        return remoteDataSource.getEvents()
    }

    suspend fun getSupabaseAthleteInfo(athleteId: String): AthleteInfo {
        return remoteDataSource.getAthleteInfo(athleteId)
    }

    suspend fun getSupabaseFavoriteEventIds(athleteId: String): List<EventIdForFavorite> {
        return remoteDataSource.getFavoriteEvents(athleteId)
    }

    suspend fun addSupabaseFavoriteEvent(athleteUuid: String, eventUuid: String) {
        remoteDataSource.addFavoriteEvent(athleteUuid, eventUuid)

    }

    suspend fun deleteSupabaseFavoriteEvent(athleteUuid: String, eventUuid: String) {
        remoteDataSource.deleteFavoriteEvent(athleteUuid, eventUuid)
    }

    suspend fun logoutSupabaseAthlete() {
        remoteDataSource.logoutAthlete()
    }

    suspend fun getSupabaseEventResultAthleteInfo(eventUuid: String): List<ResultAthleteInfo> {
        return remoteDataSource.getEventResultAthleteInfo(eventUuid)
    }

    suspend fun registerSupabaseAthleteToAnEvent(
        athleteUuid: String,
        eventUuid: String,
        category: String
    ) {
        remoteDataSource.registerAthleteToAnEvent(athleteUuid, eventUuid, category)
    }

    suspend fun getSupabaseEventResultProfileInfo(athleteUuid: String): List<EventResultProfileInfo> {
        return remoteDataSource.getEventResultProfileInfo(athleteUuid)
    }

    suspend fun getSupabaseAthleteRegisteredEvents(athleteUuid: String): List<EventInfo> {
        return remoteDataSource.getAthleteRegisteredEvents(athleteUuid)
    }

    suspend fun getSupabaseAthleteRegisteredEventsUuid(athleteUuid: String): List<String> {
        return remoteDataSource.getAthleteRegisteredEventsUuid(athleteUuid)
    }

    suspend fun updateSupabaseAthleteProfilePic(athleteUuid: String, profilePictureUrl: String): Boolean {
        return remoteDataSource.updateAthleteProfilePic(athleteUuid, profilePictureUrl)
    }

    suspend fun getSupabaseOrganizerInfo(organizerUuid: String): OrganizerInfo {
        return remoteDataSource.checkOrganizerAccountOrDefault(organizerUuid)
    }

    suspend fun getSupabaseOrganizerEvents(organizerUuid: String): List<EventInfo> {
        return remoteDataSource.getOrganizerEvents(organizerUuid)
    }

    suspend fun addSupabaseEvent(title: String,
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
        return remoteDataSource.addEvent(
            title = title,
            startDate = startDate,
            endDate = endDate,
            country = country,
            city = city,
            county = county,
            details = details,
            eventCategory = eventCategory,
            athleteCategories = athleteCategories,
            organizerUuid = organizerUuid
        )
    }
}
