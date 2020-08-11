package com.malfliet.hangaround.persistence.network.DTO

import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.network.UserMetaData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth0UserDTO(
    var email: String,
    var user_metadata: UserMetaData,
    var user_id: String
)

fun Auth0UserDTO.asDomainModel(): Person {
    return Person(
        "",
        user_metadata.name,
        email,
        mutableListOf()
    )
}