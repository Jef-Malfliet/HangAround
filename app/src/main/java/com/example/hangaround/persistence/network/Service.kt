package com.example.hangaround.persistence.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://fc9213ad4ef3.ngrok.io"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface HangAroundAPIService {

    //moet weg
    @GET("getPersons")
    fun getPersons(): Deferred<List<PersonDTO>>

    @GET("getPersonById")
    fun getPersonById(): Deferred<List<PersonDTO>>

    @GET("getPersonsWithNameLike")
    fun getPersonsWithNameLike(): Deferred<List<PersonDTO>>

    @GET("getPersonsInActivity")
    fun getPersonsInActivity(@Query("id") activityId: String): Deferred<List<PersonDTO>>

    @POST("updatePerson")
    fun updatePerson()

    //moet weg
    @GET("getActivities")
    fun getActivities(): Deferred<List<ActivityDTO>>

    @GET("getActivityById")
    fun getActivityById(@Query("id") activityId: String): Deferred<List<ActivityDTO>>

    @GET("getActivitiesByOwner")
    fun getActivitiesByOwner(): Deferred<List<ActivityDTO>>

    @GET("getActivitiesContainingPerson")
    fun getActivitiesContainingPerson(@Query("id") userId: String): Deferred<List<ActivityDTO>>

    @POST("makeActivity")
    fun makeActivity()

    @POST("updateActivity")
    fun updateActivity()
}

object HangAroundAPI {
    val service: HangAroundAPIService by lazy {
        retrofit.create(HangAroundAPIService::class.java)
    }
}