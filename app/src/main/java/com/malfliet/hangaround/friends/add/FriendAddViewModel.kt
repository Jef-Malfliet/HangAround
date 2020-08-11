package com.malfliet.hangaround.friends.add

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.repository.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FriendAddViewModel(application: Application) : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = HangAroundDatabase.getDatabase(application)

    private val personRepository = PersonRepository(database)

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    val personId = sharedPreferences.getString("personId", "")

    val persons = personRepository.persons

    fun addFriend(id: String) {
        coroutineScope.launch {
            val person = persons.value!!.find { person -> person.id == personId }!!
            val templist = mutableListOf<String>()
            templist.addAll(person.friends)
            templist.add(id)
            person.friends = templist
            personRepository.updatePerson(person)
        }
    }

    fun getPersonsWithNameLike(name: String) {
        coroutineScope.launch {
            personRepository.getPersonsWithNameLike(name)
        }
    }

    fun clearPersons() {
        coroutineScope.launch {
            personRepository.clearAll()
        }
    }

    class Factory(val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendAddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendAddViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}