package com.malfliet.hangaround.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person constructor(
    @Json(name = "_id")
    val id: String?,
    var name: String,
    val email: String,
    var friends: MutableList<String>
) : Parcelable {

}