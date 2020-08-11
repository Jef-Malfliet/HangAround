package com.malfliet.hangaround.persistence.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Participant

@Entity(tableName = "activities")
data class ActivityDE constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: List<Participant>,
    var description: String
)

fun List<ActivityDE>.asDomainModel(): List<Activity> {
    return map {
        Activity(
            it.id,
            it.name,
            it.owner,
            it.startDate,
            it.endDate,
            it.place,
            it.participants,
            it.description
        )
    }
}

fun ActivityDE.asDomainModel(): Activity {
    return Activity(
        id,
        name,
        owner,
        startDate,
        endDate,
        place,
        participants,
        description
    )
}
