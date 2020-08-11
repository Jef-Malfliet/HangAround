package com.malfliet.hangaround.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Participant(
    @Json(name = "_id")
    val id: String?,
    var role:String,
    val personId: String
) : Parcelable {

}