package com.example.hangaround.persistence.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://devops-hogent.ninja:8768/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface HangAroundAPIService {

    @GET("getPersons")
    fun getPersons(): Deferred<PersonDTOContainer>

    @GET("getActivities")
    fun getActivities(): Deferred<ActivityDTOContainer>
}

object HangAroundAPI {
    val service: HangAroundAPIService by lazy {
        retrofit.create(HangAroundAPIService::class.java)
    }
}