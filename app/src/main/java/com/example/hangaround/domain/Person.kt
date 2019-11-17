package com.example.hangaround.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Person constructor(
    @PrimaryKey
    @Json(name = "_id")
    val id: Int,
    val name: String,
    var friends: List<String>
) : Parcelable {

}

fun List<Person>.asDomainModel(): List<Person> {
    return map {
        Person(
            id = it.id,
            name = it.name,
            friends = it.friends
        )
    }
}