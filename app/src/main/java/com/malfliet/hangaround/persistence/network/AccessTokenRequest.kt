package com.malfliet.hangaround.persistence.network

class AccessTokenRequest constructor(
    val client_id: String,
    val client_secret: String,
    val audience: String,
    val grant_type: String
)