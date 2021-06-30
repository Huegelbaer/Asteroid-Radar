package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface NasaApiService {

    @GET("/planetary/apod?api_key=${BuildConfig.NASA_API_KEY}")
    fun getPictureOfDay(): Call<PictureOfDay>
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}