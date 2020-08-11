package com.malfliet.hangaround.persistence.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getPersons(): LiveData<List<PersonDE>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg persons: PersonDE)

    @Query("delete from persons")
    fun clearAll()

    @Update()
    fun updateAll(vararg persons: PersonDE)
}