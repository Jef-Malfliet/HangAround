package com.malfliet.hangaround.persistence.network.DTO

import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.database.PersonDE
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDTO(
    @Json(name = "_id")
    val id: String,
    val name: String,
    val email: String,
    var friends: MutableList<String>
)

fun List<PersonDTO>.asDatabaseModel(): Array<PersonDE> {
    return map {
        PersonDE(
            id = it.id,
            name = it.name,
            email = it.email,
            friends = it.friends
        )
    }.toTypedArray()
}

fun PersonDTO.asDomainModel(): Person {
    return Person(id, name, email, friends)
}