package com.malfliet.hangaround.persistence.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.malfliet.hangaround.persistence.network.DTO.AccessTokenDTO
import com.malfliet.hangaround.persistence.network.DTO.Auth0IdDTO
import com.malfliet.hangaround.persistence.network.DTO.Auth0UserDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://dev-7lg1fu6u.eu.auth0.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface Auth0Service {
    @GET("userinfo")
    fun getAccount(@Header("Authorization") AccessTokenBearer: String): Deferred<Auth0IdDTO>

    @GET("/api/v2/users/{id}")
    fun getUser(
        @Header("Authorization") accessJwt: String,
        @Path("id") id: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): Deferred<Auth0UserDTO>

    @POST("oauth/token")
    fun getAccessToken(
        @Header("Content-Type") contentType: String,
        @Body body: AccessTokenRequest
    ): Deferred<AccessTokenDTO>
}

object Auth0API {
    val service: Auth0Service by lazy {
        retrofit.create(Auth0Service::class.java)
    }
}