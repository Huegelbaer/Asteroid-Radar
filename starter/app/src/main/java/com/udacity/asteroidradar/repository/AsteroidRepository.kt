package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidsDatabase) {

    private val today: String
        get() {
            val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            return formatter.format(Date())
        }

    private val seventhDay: String
        get() {
            val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            return formatter.format(calendar.time)
        }

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getAsteroidsInPeriod(today, seventhDay)) {
            it?.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val jsonResult = NasaApi.retrofitService.getAsteroids()
                val asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult))
                database.asteroidDAO.insertAll(asteroids.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "Refresh asteroids failed.", e.cause)
            }
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = NasaApi.retrofitService.getPictureOfDay()
                withContext(Dispatchers.Main) {
                    _pictureOfDay.value = pictureOfDay
                }
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "Refresh picture of day failed.", e.cause)
            }
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                database.asteroidDAO.deleteAsteroidsBeforeDate(today)
            } catch (e: Exception) {
                Log.e("AsteroidRepository", "Delete asteroids older than today failed", e.cause)
            }
        }
    }
}