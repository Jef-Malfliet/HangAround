package com.example.hangaround.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "activities")
@Parcelize
data class Activity constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    var owner: String,
    val startDate: String,
    val endDate: String,
    var place: String,
    var participants: List<Participant>,
    var description: String
) : Parcelable {

}