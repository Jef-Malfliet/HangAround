package com.example.hangaround.persistence.network

import com.example.hangaround.domain.Activity
import com.example.hangaround.domain.Person
import com.squareup.moshi.JsonClass

//Activity
@JsonClass(generateAdapter = true)
data class ActivityDTOContainer(val activities: List<ActivityDTO>)

@JsonClass(generateAdapter = true)
data class ActivityDTO(
    val id: Int,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: Map<String, String>,
    var description: String
)

fun ActivityDTOContainer.asDatabaseModel(): Array<Activity> {
    return activities.map {
        Activity(
            id = it.id,
            name = it.name,
            owner = it.owner,
            startDate = it.startDate,
            endDate = it.endDate,
            place = it.place,
            participants = it.participants,
            description = it.description
        )
    }.toTypedArray()
}

//Person
@JsonClass(generateAdapter = true)
data class PersonDTOContainer(val activities: List<PersonDTO>)

@JsonClass(generateAdapter = true)
data class PersonDTO(
    val id: Int,
    val name: String,
    var friends: List<String>
)

fun PersonDTOContainer.asDatabaseModel(): Array<Person> {
    return activities.map {
        Person(
            id = it.id,
            name = it.name,
            friends = it.friends
        )
    }.toTypedArray()
}
