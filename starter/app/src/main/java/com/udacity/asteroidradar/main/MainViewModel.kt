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

    val pictureOfDayUrl = Transformations.map(repository.pictureOfDay) { picture ->
        if (picture?.mediaType == "image") {
            picture.url
        }
        null
    }

    init {
        getPictureOfDay()
        getAsteroids()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            repository.refreshPictureOfDay()
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