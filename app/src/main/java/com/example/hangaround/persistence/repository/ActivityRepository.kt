package com.example.hangaround.persistence.repository

import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivityRepository(private val database: HangAroundDatabase) {

    val activities = database.activityDao.getActivities()

    suspend fun refreshActivities() {
        withContext(Dispatchers.IO) {
            val activityDTOList = HangAroundAPI.service.getActivities().await()
            database.activityDao.insertAll(*activityDTOList.asDatabaseModel())
        }
    }

    suspend fun getActivity(id: String) {
        withContext(Dispatchers.IO) {
            val personDTOList =
                HangAroundAPI.service.getPersonsInActivity(activityId = id).await()
            database.personDao.insertAll(*personDTOList.asDatabaseModel())
        }
    }
}