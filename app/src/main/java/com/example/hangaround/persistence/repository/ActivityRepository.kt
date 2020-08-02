package com.example.hangaround.persistence.repository

import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

class ActivityRepository(private val database: HangAroundDatabase) {

    val activities = database.activityDao.getActivities()

    suspend fun refreshActivities() {
        withContext(Dispatchers.IO) {
            try {
                val activityDTOList = HangAroundAPI.service.getActivities().await()
                database.activityDao.insertAll(*activityDTOList.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun getActivity(id: String) {
        withContext(Dispatchers.IO) {
            val activity = database.activityDao.getActivity(id)
            if (activity.value == null) {
                try {
                    val activityDTO = HangAroundAPI.service.getActivityById(id).await()
                    database.activityDao.insertAll(*activityDTO.asDatabaseModel())
                } catch (ex: HttpException) {
                    Timber.d(ex)
                }
            }
        }
    }
}