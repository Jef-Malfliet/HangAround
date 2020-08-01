package com.example.hangaround.persistence.database

import androidx.room.TypeConverter
import com.example.hangaround.domain.Participant
import com.google.gson.Gson
import com.squareup.moshi.ToJson

class Converters {

    @TypeConverter
    fun MapToString(map: Map<String, String>): String {
        var mapString: String = "";
        for ((key, value) in map) {
            mapString.plus(String.format("%s,%s;", key, value))
        }
        return mapString.removeSuffix(";")
    }

    @TypeConverter
    fun StringToMap(string: String): Map<String, String> {
        var pairs = string.split(";")
        var map = emptyMap<String, String>().toMutableMap()

        for (pair in pairs) {
            var keyValue = pair.split(",")
            map[keyValue[0]] = keyValue[1]
        }

        return map.toMap()
    }

    @TypeConverter
    fun stringListToString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        return string.split(",", ignoreCase = true).map { a -> a.trim() }.toList()
    }


    @TypeConverter
    fun participantListToString(participants: List<Participant>): String {
        var listString = "";
        if (participants.isNotEmpty()) {
            for (participant in participants) {
                listString = listString.plus(
                    String.format(
                        "%s,%s,%s;",
                        participant.id,
                        participant.role,
                        participant.personId
                    )
                )
            }
        }
        return listString.removeSuffix(";")
    }

    @TypeConverter
    fun stringToParticipantList(string: String): List<Participant> {
        var list = emptyList<Participant>().toMutableList()

        if (string.isNotEmpty()) {
            var participants = string.split(";")
            for (participant in participants) {
                var values = participant.split(",")
                list.add(Participant(values[0], values[1], values[2]))
            }
        }

        return list.toList()
    }
}