package com.malfliet.hangaround.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Database
@Database(entities = [ActivityDE::class, PersonDE::class], version = 6, exportSchema = false)
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