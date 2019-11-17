package com.example.hangaround.persistence.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Person

//DAOs
@Dao
interface ActvityDao{
    @Query("SELECT * FROM activity")
    fun getActivities(): LiveData<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg activities: Activity)
}

@Dao
interface PersonDao{
    @Query("SELECT * FROM person")
    fun getPersons(): LiveData<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg persons: Person)
}

//Database
@Database(entities = [Activity::class, Person::class], version = 1)
abstract class HangAroundDatabase: RoomDatabase(){
    abstract val activityDao: ActvityDao
    abstract val personDao: PersonDao
}

private lateinit var INSTANCE: HangAroundDatabase

fun getDatabase(context: Context): HangAroundDatabase {
    synchronized(HangAroundDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                HangAroundDatabase::class.java, "HangAroundDB").build()
        }
    }
    return INSTANCE
}