package com.example.hangaround.persistence.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hangaround.domain.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getPersons(): LiveData<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg persons: Person)
}