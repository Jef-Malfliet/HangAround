package com.malfliet.hangaround.persistence.network.DTO

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenDTO(
    var access_token: String,
    var scope: String,
    var expires_in: Int,
    var token_type: String
)