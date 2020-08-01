package com.example.hangaround.persistence.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hangaround.domain.Activity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities")
    fun getActivities(): LiveData<List<Activity>>

    @Query("SELECT * FROM activities WHERE id == :activityId")
    fun getActivity(activityId: String): LiveData<Activity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg activities: Activity)
}