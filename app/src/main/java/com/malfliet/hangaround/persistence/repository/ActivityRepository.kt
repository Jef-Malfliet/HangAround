package com.malfliet.hangaround.persistence.repository

import androidx.lifecycle.Transformations
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.database.asDomainModel
import com.malfliet.hangaround.persistence.network.HangAroundAPI
import com.malfliet.hangaround.persistence.network.DTO.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ActivityRepository(private val database: HangAroundDatabase) {

    val activities = Transformations.map(database.activityDao.getActivities()) {
        it.asDomainModel()
    }

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
            try {
                val activityDTO = HangAroundAPI.service.getActivityById(id).await()
                database.activityDao.insertAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun insertActivity(activity: Activity) {
        withContext(Dispatchers.IO) {
            try {
                val activityDTO = HangAroundAPI.service.makeActivity(activity).await()
                database.activityDao.insertAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun updateActivity(activity: Activity) {
        withContext(Dispatchers.IO) {
            try {
                val activityDTO = HangAroundAPI.service.updateActivity(activity).await()
                database.activityDao.updateAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun deleteActivity(activityId: String) {
        withContext(Dispatchers.IO) {
            try {
                val activityDTO = HangAroundAPI.service.deleteActivity(activityId).await()
                database.activityDao.deleteAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            database.activityDao.clearAll()
        }
    }

    suspend fun getActivitiesContainingPerson(personId: String) {
        withContext(Dispatchers.IO) {
            try {
                val activityDTO =
                    HangAroundAPI.service.getActivitiesContainingPerson(personId).await()
                database.activityDao.insertAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }

    suspend fun getActivitiesByOwner(personId: String) {
        withContext(Dispatchers.IO) {
            try {
                val activityDTO =
                    HangAroundAPI.service.getActivitiesByOwner(personId).await()
                database.activityDao.insertAll(*activityDTO.asDatabaseModel())
            } catch (ex: Exception) {
                Timber.d(ex)
            }
        }
    }
}