package com.malfliet.hangaround.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.malfliet.hangaround.persistence.database.PersonDao
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.utilities.personDE1
import com.malfliet.hangaround.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonDAOTest {
    private lateinit var database: HangAroundDatabase
    private lateinit var personDao: PersonDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, HangAroundDatabase::class.java).build()
        personDao = database.personDao
        personDao.insertAll(*arrayOf(personDE1))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetPersons() {
        val persons = getValue(personDao.getPersons())
        Assert.assertThat(persons.size, Matchers.equalTo(1))
        Assert.assertThat(persons[0], Matchers.equalTo(personDE1))
    }

    @Test
    fun testClearDb() {
        personDao.clearAll()
        val persons = getValue(personDao.getPersons())
        Assert.assertThat(persons.size, Matchers.equalTo(0))
    }
}