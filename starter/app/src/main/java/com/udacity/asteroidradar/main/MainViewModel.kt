package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(val application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    val asteroids = repository.asteroids


    private var _selectedAsteroid = MutableLiveData<Asteroid?>()

    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid

    private var _pictureOfDay = MutableLiveData<PictureOfDay?>()

    val pictureOfDayUrl: LiveData<String?> = Transformations.map(_pictureOfDay) { picture ->
        if (picture?.mediaType == "image") {
            picture.url
        }
        null
    }

    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    init {
        //getPictureOfDay()
        getAsteroids()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            Log.d("MainViewModel", "Start loading picture of day")
            NasaApi.retrofitService.getPictureOfDay().enqueue(object : Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    Log.d("MainViewModel", "Load picture of day succeed.")
                    _pictureOfDay.value = response.body()
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    Log.d("MainViewModel", "Load picture of day failed.", t)
                    _pictureOfDay.value = null
                }
            })
        }
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    fun onSelected(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun onNavigationCompleted() {
        _selectedAsteroid.value = null
    }
}