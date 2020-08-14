package com.malfliet.hangaround.profile

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.repository.ActivityRepository
import com.malfliet.hangaround.persistence.repository.LoginRepository
import com.malfliet.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private val personRepository = PersonRepository(database)
    private val activityRepository = ActivityRepository(database)
    private val loginRepository = LoginRepository(application, database)


    var personId = sharedPreferences.getString("personId", "")
    var persons = personRepository.persons

    fun logout() {
        coroutineScope.launch {
            personRepository.clearAll()
            activityRepository.clearAll()
        }
        loginRepository.logout()
    }

    fun getPerson(personId: String) {
        coroutineScope.launch {
            personRepository.getPerson(personId)
        }
    }

    fun updatePerson(person: Person) {
        coroutineScope.launch {
            personRepository.updatePerson(person)
        }
    }

    class Factory(val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}