package com.malfliet.hangaround.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.malfliet.hangaround.persistence.database.ActivityDao
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.utilities.activityDE1
import com.malfliet.hangaround.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityDAOTest {
    private lateinit var database: HangAroundDatabase
    private lateinit var activityDao: ActivityDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, HangAroundDatabase::class.java).build()
        activityDao = database.activityDao
        activityDao.insertAll(*arrayOf(activityDE1))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetActivities() {
        val activities = getValue(activityDao.getActivities())
        Assert.assertThat(activities.size, Matchers.equalTo(1))
        Assert.assertThat(activities[0], Matchers.equalTo(activityDE1))
    }

    @Test
    fun testGetActivity() {
        val activity = getValue(activityDao.getActivity("5e00e8f0520fe30025c17e62"))
        Assert.assertThat(activity, Matchers.equalTo(activityDE1))
    }

    @Test
    fun testClearDb() {
        activityDao.clearAll()
        val activities = getValue(activityDao.getActivities())
        Assert.assertThat(activities.size, Matchers.equalTo(0))
    }
}