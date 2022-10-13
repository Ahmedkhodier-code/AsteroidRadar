package com.udacity.asteroidradar.api

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.KEY_URL
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//enum class ApiFilter(val value: String) { START_DATE(""), END_DATE("") }

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface NasaApiService {

    @GET("planetary/apod?api_key=" + KEY_URL)
    suspend fun getPictureOfDay2(): PictureOfDay

    @GET("/neo/rest/v1/feed?start_date=&end_date=&api_key=" + KEY_URL)
    //@Query("start_date") start_date: String,@Query("end_date") end_date: String
    suspend fun getAsteroid(): String
    //suspend fun getAsteroid(start_date: String, end_date: String, KEY_URL: String): String
    // fun getAsteroid(): Deferred<NetworkAsteroidContainer>
}

object NasaApi {
    val retrofitService = retrofit.create(NasaApiService::class.java)
}