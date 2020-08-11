package com.malfliet.hangaround.domain

import android.os.Parcelable
import com.malfliet.hangaround.persistence.database.PersonDE
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

fun Person.asDatabaseModel(): PersonDE {
    return PersonDE(id!!, name, email, friends)
}