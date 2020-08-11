package com.malfliet.hangaround.persistence.network.DTO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterDTO(
    val name: String,
    val email: String,
    val friends: List<String>
) : Parcelable {

}
