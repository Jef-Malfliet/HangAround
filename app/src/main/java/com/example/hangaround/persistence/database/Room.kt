package com.example.hangaround.persistence.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Person

//Database
@Database(entities = [Activity::class, Person::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HangAroundDatabase : RoomDatabase() {
    abstract val activityDao: ActivityDao
    abstract val personDao: PersonDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: HangAroundDatabase

        fun getDatabase(context: Context): HangAroundDatabase {
            synchronized(HangAroundDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HangAroundDatabase::class.java, "HangAroundDB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}