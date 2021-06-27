package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private var _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    private var _selectedAsteroid = MutableLiveData<Asteroid?>()

    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid


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
                false))
    }


    fun onSelected(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun onNavigationCompleted() {
        _selectedAsteroid.value = null
    }
}