package com.example.hangaround.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Participant constructor(
    val id: String,
    val role :String,
    val personId: String
) : Parcelable {

}