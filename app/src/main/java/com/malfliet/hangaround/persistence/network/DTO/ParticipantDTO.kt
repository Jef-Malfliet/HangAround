package com.malfliet.hangaround.persistence.network.DTO

import com.malfliet.hangaround.domain.Participant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParticipantDTO(
    @Json(name = "_id")
    val id: String,
    val role :String,
    val personId: String
)

fun List<ParticipantDTO>.toParticipantList(): List<Participant> {
    return map {
        Participant(
            id = it.id,
            role = it.role,
            personId = it.personId
        )
    }.toList()
}