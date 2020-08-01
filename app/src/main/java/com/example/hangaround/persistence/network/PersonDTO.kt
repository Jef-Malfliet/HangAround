package com.example.hangaround.persistence.network

import com.example.hangaround.domain.Person
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDTO(
    @Json(name = "_id")
    val id: String,
    val name: String,
    var friends: List<String>
)

fun List<PersonDTO>.asDatabaseModel(): Array<Person> {
    return map {
        Person(
            id = it.id,
            name = it.name,
            friends = it.friends
        )
    }.toTypedArray()
}
