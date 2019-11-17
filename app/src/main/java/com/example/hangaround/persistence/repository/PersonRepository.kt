package com.example.hangaround.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.hangaround.domain.Person
import com.example.hangaround.domain.asDomainModel
import com.example.hangaround.persistence.database.HangAroundDatabase
import com.example.hangaround.persistence.network.HangAroundAPI
import com.example.hangaround.persistence.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonRepository(private val database: HangAroundDatabase){

    val persons : LiveData<List<Person>> = Transformations.map(database.personDao.getPersons()){
        it.asDomainModel()
    }

    suspend fun refreshPersons(){
        withContext(Dispatchers.IO){
            val personList = HangAroundAPI.service.getPersons().await()
            database.personDao.insertAll(*personList.asDatabaseModel())
        }
    }
}