package com.malfliet.hangaround.persistence.network.DTO

import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.persistence.database.ActivityDE
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityDTO(
    @Json(name = "_id")
    val id: String,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: List<ParticipantDTO>,
    var description: String
)

fun List<ActivityDTO>.asDatabaseModel(): Array<ActivityDE> {
    return map {
        ActivityDE(
            id = it.id,
            name = it.name,
            owner = it.owner,
            startDate = it.startDate,
            endDate = it.endDate,
            place = it.place,
            participants = it.participants.toParticipantList(),
            description = it.description
        )
    }.toTypedArray()
}

fun ActivityDTO.asDomainModel(): Activity {
    return Activity(
        id,
        name,
        owner,
        startDate,
        endDate,
        place,
        participants.toParticipantList(),
        description
    )
}