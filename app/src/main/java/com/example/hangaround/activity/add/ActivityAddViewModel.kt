package com.example.hangaround.activity.add

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hangaround.domain.Person
import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.repository.ActivityRepository
import com.example.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityAddViewModel(application: Application, private var activityId: String) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val personRepository = PersonRepository(database)
    private val activityRepository = ActivityRepository(database)

    val activity = database.activityDao.getActivity(activityId)

    private val _persons = personRepository.persons
    val persons: LiveData<List<Person>>
        get() = _persons

    fun getActivity() {
        coroutineScope.launch {
            activityRepository.getActivity(activityId)
        }
    }

    fun getParticipants() {
        coroutineScope.launch {
            personRepository.getParticipants(activityId)
        }
    }

    class Factory(val app: Application, private val activityId: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActivityAddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ActivityAddViewModel(
                    app, activityId
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}