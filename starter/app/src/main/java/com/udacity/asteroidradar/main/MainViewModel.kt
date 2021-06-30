package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(val application: Application) : ViewModel() {

    private val _database = getDatabase(application)

    private var _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


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
        _asteroids.value = listOf(
            Asteroid(1, "1", "2021-01-03",
                0.0, 0.2, 1.2, 1.4,
                false),
            Asteroid(2, "2", "2021-01-04",
                0.0, 0.2, 1.2, 1.4,
                true),
            Asteroid(3, "3", "2021-01-05",
                0.0, 0.2, 1.2, 1.4,
                false),
            Asteroid(4, "4", "2021-01-06",
                0.0, 0.2, 1.2, 1.4,
                true),
            Asteroid(5, "5", "2021-01-07",
                0.0, 0.2, 1.2, 1.4,
                false)
        )

        getPictureOfDay()
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

    fun onSelected(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun onNavigationCompleted() {
        _selectedAsteroid.value = null
    }
}