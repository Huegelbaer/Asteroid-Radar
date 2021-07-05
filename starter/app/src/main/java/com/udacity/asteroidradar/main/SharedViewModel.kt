package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.model.Asteroid

class SharedViewModel: ViewModel() {

    val selectedAsteroid = MutableLiveData<Asteroid?>()
}