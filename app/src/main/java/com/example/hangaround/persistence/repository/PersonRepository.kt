package com.example.hangaround.persistence.repository

import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonRepository(private val database: HangAroundDatabase) {

    val persons = database.personDao.getPersons()

    suspend fun refreshPersons() {
        withContext(Dispatchers.IO) {
            val personDTOList = HangAroundAPI.service.getPersons().await()
            database.personDao.insertAll(*personDTOList.asDatabaseModel())
        }
    }

    suspend fun getParticipants(id: String) {
        withContext(Dispatchers.IO) {
            val personDTOList =
                HangAroundAPI.service.getPersonsInActivity(activityId = id).await()
            database.personDao.insertAll(*personDTOList.asDatabaseModel())
        }
    }
}