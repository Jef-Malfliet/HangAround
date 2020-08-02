package com.example.hangaround.persistence.repository

import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class PersonRepository(private val database: HangAroundDatabase) {

    val persons = database.personDao.getPersons()

    suspend fun refreshPersons() {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.getPersons().await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun getParticipants(id: String) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList =
                    HangAroundAPI.service.getPersonsInActivity(activityId = id).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }
}