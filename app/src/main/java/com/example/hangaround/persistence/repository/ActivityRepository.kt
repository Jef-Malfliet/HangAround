package com.example.hangaround.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.asDomainModel
import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivityRepository(private val database: HangAroundDatabase){

    val activities : LiveData<List<Activity>> = Transformations.map(database.activityDao.getActivities()){
        it.asDomainModel()
    }

    suspend fun refreshActivities(){
        withContext(Dispatchers.IO){
            val activityList = HangAroundAPI.service.getActivities().await()
            database.activityDao.insertAll(*activityList.asDatabaseModel())
        }
    }
}