package io.github.usk83.contacts.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://randomuser.me/api/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ContactsApiService {
    @GET("?nat=ca")
    suspend fun getProperties(@Query("results") num: Int): ContactsProperties
}

object ContactsApi {
    val retrofitService : ContactsApiService by lazy {
        retrofit.create(ContactsApiService::class.java) }
}
