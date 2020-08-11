package com.malfliet.hangaround.activity.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.HangAroundDatabase.Companion.getDatabase
import com.malfliet.hangaround.persistence.repository.ActivityRepository
import com.malfliet.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityListViewModel(application: Application) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)

    private val activityRepository = ActivityRepository(database)
    private val personRepository = PersonRepository(database)

    private val _activities = activityRepository.activities
    val activities: LiveData<List<Activity>>
        get() = _activities

    private val _persons = personRepository.persons
    val persons: LiveData<List<Person>>
        get() = _persons

    fun clearAll() {
        coroutineScope.launch {
            activityRepository.clearAll()
        }
    }

    fun refreshPersons() {
        coroutineScope.launch {
            personRepository.refreshPersons()
        }
    }

    fun getActivitiesContainingPerson(personId: String) {
        coroutineScope.launch {
            activityRepository.getActivitiesContainingPerson(personId)
        }
    }

    fun getActivitiesByOwner(personId: String) {
        coroutineScope.launch {
            activityRepository.getActivitiesByOwner(personId)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActivityListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ActivityListViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}