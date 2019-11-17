package com.example.hangaround.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hangaround.persistence.network.ActivityDTOContainer
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity
data class Activity constructor(
    @PrimaryKey
    @Json(name = "_id")
    val id: Int,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: Map<String, String>,
    var description: String
) : Parcelable {

}

fun List<Activity>.asDomainModel(): List<Activity> {
    return map {
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
    }
}