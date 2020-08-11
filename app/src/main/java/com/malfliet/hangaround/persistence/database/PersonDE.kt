package com.malfliet.hangaround.persistence.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malfliet.hangaround.domain.Person

@Entity(tableName = "persons")
data class PersonDE constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    var friends: MutableList<String>
)

fun List<PersonDE>.asDomainModel(): List<Person> {
    return map {
        Person(it.id, it.name, it.email, it.friends)
    }
}