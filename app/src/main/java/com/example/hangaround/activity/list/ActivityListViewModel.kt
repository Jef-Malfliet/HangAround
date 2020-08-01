package com.example.hangaround.activity.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Person
import com.example.hangaround.persistence.database.HangAroundDatabase.Companion.getDatabase
import com.example.hangaround.persistence.repository.ActivityRepository
import com.example.hangaround.persistence.repository.PersonRepository
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

    fun refreshActivities() {
        coroutineScope.launch {
            activityRepository.refreshActivities()
        }
    }

    fun refreshPersons() {
        coroutineScope.launch {
            personRepository.refreshPersons()
        }
    }

    private val _navigateToActivityDetail = MutableLiveData<String>()
    val navigateToActivityDetail
        get() = _navigateToActivityDetail

    fun onActivityClicked(id: String){
        _navigateToActivityDetail.value = id
    }

    fun onActivityDetailNavigated() {
        _navigateToActivityDetail.value = null
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