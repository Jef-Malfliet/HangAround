package com.malfliet.hangaround.friends.list

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FriendListViewModel(application: Application) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val personRepository = PersonRepository(database)

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    val personId = sharedPreferences.getString("personId", "")

    val persons = personRepository.persons

    fun getFriends() {
        coroutineScope.launch {
            personRepository.getFriends(sharedPreferences.getString("personId", "")!!)
        }
    }

    fun removeFriend(id: String) {
        coroutineScope.launch {
            val person = persons.value!!.find { person -> person.id == personId }!!
            val templist = mutableListOf<String>()
            templist.addAll(person.friends)
            templist.remove(id)
            person.friends = templist
            personRepository.updatePerson(person)
        }
    }

    class Factory(val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendListViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}