package com.malfliet.hangaround.persistence.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.malfliet.hangaround.R
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.network.*
import com.malfliet.hangaround.persistence.network.DTO.LoginDTO
import com.malfliet.hangaround.persistence.network.DTO.RegisterDTO
import com.malfliet.hangaround.persistence.network.DTO.asDomainModel
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.net.SocketTimeoutException

class LoginRepository(val context: Context, val database: HangAroundDatabase) {

    private val activityRepository = ActivityRepository(database)
    private val personRepository = PersonRepository(database)
    var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun login(accessToken: String) {
        runBlocking {
            val auth0id = Auth0API.service.getAccount("Bearer $accessToken").await()

            val body = AccessTokenRequest(
                context.resources.getString(R.string.com_auth0_management_id),
                context.resources.getString(R.string.com_auth0_management_secret),
                context.resources.getString(R.string.com_auth0_audience),
                "client_credentials"
            )

            val accessJWT = Auth0API.service.getAccessToken("application/json", body).await()

            val person =
                Auth0API.service.getUser("Bearer " + accessJWT.access_token, auth0id.sub).await()
                    .asDomainModel()

            var personExists = false
            try {
                personExists = HangAroundAPI.service.checkPersonExists(person.email).await()
            } catch (e: SocketTimeoutException) {
                personExists = HangAroundAPI.service.checkPersonExists(person.email).await()
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }

            if (personExists) {
                try {
                    val loggedInPerson =
                        HangAroundAPI.service.loginPerson(LoginDTO(person.email)).await()
                    setLoggedInPerson(loggedInPerson[0].asDomainModel())
                } catch (e: SocketTimeoutException) {
                    val loggedInPerson =
                        HangAroundAPI.service.loginPerson(LoginDTO(person.email)).await()
                    setLoggedInPerson(loggedInPerson[0].asDomainModel())
                } catch (e: Exception) {
                    Timber.d(e)
                    e.printStackTrace()
                }
            } else {
                try {
                    val loggedInPerson = HangAroundAPI.service.registerPerson(
                        RegisterDTO(
                            person.name,
                            person.email,
                            person.friends
                        )
                    ).await()
                    setLoggedInPerson(loggedInPerson[0].asDomainModel())
                } catch (e: SocketTimeoutException) {
                    val loggedInPerson = HangAroundAPI.service.registerPerson(
                        RegisterDTO(
                            person.name,
                            person.email,
                            person.friends
                        )
                    ).await()
                    setLoggedInPerson(loggedInPerson[0].asDomainModel())
                } catch (e: Exception) {
                    Timber.d(e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun logout() {
        runBlocking {
            personRepository.clearAll()
            activityRepository.clearAll()
        }
        editor.apply {
            putString("personId", "")
            putString("email", "")
        }
        editor.commit()
    }

    private fun setLoggedInPerson(person: Person) {
        editor.apply {
            putString("personId", person.id)
            putString("email", person.email)
        }
        editor.commit()
        runBlocking {
            personRepository.refreshPersons()
            activityRepository.getActivitiesContainingPerson(person.id!!)
            activityRepository.getActivitiesByOwner(person.id!!)
        }
    }
}