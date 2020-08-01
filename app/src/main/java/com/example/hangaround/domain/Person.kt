package com.example.hangaround.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "persons")
@Parcelize
data class Person constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    var friends: List<String>
) : Parcelable {

}