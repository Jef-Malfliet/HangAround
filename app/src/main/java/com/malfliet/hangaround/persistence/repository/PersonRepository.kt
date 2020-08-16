package com.malfliet.hangaround.persistence.repository

import androidx.lifecycle.Transformations
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.HangAroundDatabase
import com.malfliet.hangaround.persistence.database.PersonDE
import com.malfliet.hangaround.persistence.database.asDomainModel
import com.malfliet.hangaround.persistence.network.HangAroundAPI
import com.malfliet.hangaround.persistence.network.DTO.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.SocketTimeoutException

class PersonRepository(private val database: HangAroundDatabase) {

    val persons = Transformations.map(database.personDao.getPersons()) {
        it.asDomainModel()
    }

    suspend fun refreshPersons() {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.getPersons().await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList = HangAroundAPI.service.getPersons().await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }

    suspend fun getParticipants(id: String) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList =
                    HangAroundAPI.service.getPersonsInActivity(id).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList =
                    HangAroundAPI.service.getPersonsInActivity(id).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }

    suspend fun getFriends(id: String) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.getFriendsOfPerson(id).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList = HangAroundAPI.service.getFriendsOfPerson(id).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            database.personDao.clearAll()
        }
    }

    suspend fun getPerson(personId: String) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.getPersonById(personId).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList = HangAroundAPI.service.getPersonById(personId).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }

    suspend fun updatePerson(person: Person) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.updatePerson(person).await()
                database.personDao.updateAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList = HangAroundAPI.service.updatePerson(person).await()
                database.personDao.updateAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }

    suspend fun getPersonsWithNameLike(name: String) {
        withContext(Dispatchers.IO) {
            try {
                val personDTOList = HangAroundAPI.service.getPersonsWithNameLike(name).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                val personDTOList = HangAroundAPI.service.getPersonsWithNameLike(name).await()
                database.personDao.insertAll(*personDTOList.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
    }
}
