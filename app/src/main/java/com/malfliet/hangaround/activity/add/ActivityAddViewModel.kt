package com.malfliet.hangaround.activity.add

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Participant
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.ActivityDE
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.repository.ActivityRepository
import com.malfliet.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ActivityAddViewModel(application: Application, private var activityId: String?) :
    ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val personRepository = PersonRepository(database)
    private val activityRepository = ActivityRepository(database)

    var activityToEdit: LiveData<ActivityDE>? = null

    var participants: MutableLiveData<MutableList<Participant>> =
        MutableLiveData<MutableList<Participant>>().default(mutableListOf())

    private val _persons = personRepository.persons
    val persons: LiveData<List<Person>>
        get() = _persons

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    val personId = sharedPreferences.getString("personId", "")

    fun getActivity() {
        coroutineScope.launch {
            if (!activityId.isNullOrEmpty()) {
                activityRepository.getActivity(activityId!!)
            }
        }
    }

    fun getParticipants() {
        coroutineScope.launch {
            if (!activityId.isNullOrEmpty()) {
                personRepository.getParticipants(activityId!!)
            }
        }
    }

    init {
        if (!activityId.isNullOrEmpty()) {
            activityToEdit = database.activityDao.getActivity(activityId!!)
        }
    }

    fun createActivity(
        name: String,
        description: String,
        place: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String
    ): Boolean {
        val startDateString = String.format("%sT%s:00.000Z", formatDateString(startDate), startTime)
        val endDateString = String.format("%sT%s:00.000Z", formatDateString(endDate), endTime)

        val newActivity =
            Activity(
                null,
                name,
                personId!!,
                startDateString,
                endDateString,
                place,
                participants.value!!,
                description
            )

        try {
            coroutineScope.launch {
                activityRepository.insertActivity(newActivity)
            }
            return true
        } catch (ex: Exception) {
            Timber.d(ex)
        }
        return false
    }

    fun updateActivity(
        id: String,
        name: String,
        description: String,
        place: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String
    ): Boolean {
        val startDateString = String.format("%sT%s:00.000Z", formatDateString(startDate), startTime)
        val endDateString = String.format("%sT%s:00.000Z", formatDateString(endDate), endTime)

        val newActivity =
            Activity(
                id,
                name,
                personId!!,
                startDateString,
                endDateString,
                place,
                participants.value!!,
                description
            )

        try {
            coroutineScope.launch {
                activityRepository.updateActivity(newActivity)
            }
            return true
        } catch (ex: Exception) {
            Timber.d(ex)
        }
        return false
    }

    private fun formatDateString(date: String): String {
        val pieces = date.split("-")
        return String.format("%s-%s-%s", pieces[0], pieces[1], pieces[2])
    }

    class Factory(val app: Application, private val activityId: String?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActivityAddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ActivityAddViewModel(app, activityId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
fun <T : Any?> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}