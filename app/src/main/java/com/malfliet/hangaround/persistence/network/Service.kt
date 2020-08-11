package com.malfliet.hangaround.persistence.network

import com.malfliet.hangaround.domain.Activity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.persistence.network.DTO.ActivityDTO
import com.malfliet.hangaround.persistence.network.DTO.LoginDTO
import com.malfliet.hangaround.persistence.network.DTO.PersonDTO
import com.malfliet.hangaround.persistence.network.DTO.RegisterDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://804aa8190733.ngrok.io"

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
    fun getPersonById(@Query("id") personId: String): Deferred<List<PersonDTO>>

    @GET("getPersonsWithNameLike")
    fun getPersonsWithNameLike(@Query("name") name: String): Deferred<List<PersonDTO>>

    @GET("getPersonsInActivity")
    fun getPersonsInActivity(@Query("id") activityId: String): Deferred<List<PersonDTO>>

    @POST("updatePerson")
    fun updatePerson(@Body person: Person): Deferred<List<PersonDTO>>

    //moet weg
    @GET("getActivities")
    fun getActivities(): Deferred<List<ActivityDTO>>

    @GET("getActivityById")
    fun getActivityById(@Query("id") activityId: String): Deferred<List<ActivityDTO>>

    @GET("getActivitiesByOwner")
    fun getActivitiesByOwner(@Query("owner") personId: String): Deferred<List<ActivityDTO>>

    @GET("getActivitiesContainingPerson")
    fun getActivitiesContainingPerson(@Query("id") personId: String): Deferred<List<ActivityDTO>>

    @POST("makeActivity")
    fun makeActivity(@Body activity: Activity): Deferred<List<ActivityDTO>>

    @POST("updateActivity")
    fun updateActivity(@Body activity: Activity): Deferred<List<ActivityDTO>>

    @GET("getFriendsOfPerson")
    fun getFriendsOfPerson(@Query("id") personId: String): Deferred<List<PersonDTO>>

    @DELETE("deleteActivity")
    fun deleteActivity(@Query("id") activityId: String): Deferred<List<ActivityDTO>>

    @GET("checkPersonExists")
    fun checkPersonExists(@Query("email") email: String): Deferred<Boolean>

    @POST("loginPerson")
    fun loginPerson(@Body loginDTO: LoginDTO): Deferred<List<PersonDTO>>

    @POST("registerPerson")
    fun registerPerson(@Body registerDTO: RegisterDTO): Deferred<List<PersonDTO>>
}

object HangAroundAPI {
    val service: HangAroundAPIService by lazy {
        retrofit.create(HangAroundAPIService::class.java)
    }
}