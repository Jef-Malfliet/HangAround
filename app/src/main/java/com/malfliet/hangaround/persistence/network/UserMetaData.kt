package com.malfliet.hangaround.persistence.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserMetaData(
    var name: String
)