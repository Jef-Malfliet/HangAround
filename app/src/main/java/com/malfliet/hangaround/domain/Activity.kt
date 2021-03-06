package com.malfliet.hangaround.domain

import android.os.Parcelable
import com.malfliet.hangaround.persistence.database.ActivityDE
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Activity constructor(
    @Json(name = "_id")
    val id: String?,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: List<Participant>,
    var description: String
) : Parcelable {

}

fun Activity.asDatabaseModel(): ActivityDE {
    return ActivityDE(
        id!!,
        name,
        owner,
        startDate,
        endDate,
        place,
        participants,
        description
    )
}