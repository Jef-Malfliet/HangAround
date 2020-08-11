package com.malfliet.hangaround.persistence.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities")
    fun getActivities(): LiveData<List<ActivityDE>>

    @Query("SELECT * FROM activities WHERE id = :id")
    fun getActivity(id: String): LiveData<ActivityDE>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg activities: ActivityDE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: ActivityDE)

    @Update()
    fun updateAll(vararg activity: ActivityDE)

    @Delete
    fun deleteAll(vararg activity: ActivityDE)

    @Query("delete from activities")
    fun clearAll()
}