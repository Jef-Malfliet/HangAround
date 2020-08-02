package com.example.hangaround.activity.detail

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

class ActivityDetailViewModel(application: Application, private val activityId: String) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val personRepository = PersonRepository(database)
    private val activityRepository = ActivityRepository(database)

    private val _persons = personRepository.persons
    val persons: LiveData<List<Person>>
        get() = _persons

    val activity = database.activityDao.getActivity(activityId)

    fun getParticipants() {
        coroutineScope.launch {
            personRepository.getParticipants(activityId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application, private val activityId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActivityDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ActivityDetailViewModel(
                    app, activityId
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}