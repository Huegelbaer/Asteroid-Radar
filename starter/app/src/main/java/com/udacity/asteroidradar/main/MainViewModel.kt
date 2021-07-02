package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(val application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    val asteroids = repository.asteroids


    private var _selectedAsteroid = MutableLiveData<Asteroid?>()

    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid

    val pictureOfDayContentDescription: LiveData<String> = Transformations.map(repository.pictureOfDay) { picture ->
        picture?.let {
            val formatter = application.getString(R.string.nasa_picture_of_day_content_description_format)
            return@map String.format(formatter, it.title)
        }
        return@map application.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }

    val pictureOfDayUrl: LiveData<String> = Transformations.map(repository.pictureOfDay) { picture ->
        if (picture?.mediaType == "image") {
            return@map picture.url
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