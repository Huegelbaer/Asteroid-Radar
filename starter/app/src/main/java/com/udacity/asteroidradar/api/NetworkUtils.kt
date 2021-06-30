package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjects = jsonResult.getJSONObject("near_earth_objects")
    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

    for (formattedDate in nextSevenDaysFormattedDates) {
        val asteroidJsonArray = nearEarthObjects.getJSONArray(formattedDate)

        for (i in 0 until asteroidJsonArray.length()) {
            val asteroidJson = asteroidJsonArray.getJSONObject(i)

            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson
                .getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson
                .getJSONObject("estimated_diameter")
                .getJSONObject("kilometers")
                .getDouble("estimated_diameter_max")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val closeApproachDateObject = asteroidJson
                .getJSONArray("close_approach_data")
                .getJSONObject(0)
            val relativeVelocity = closeApproachDateObject
                .getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachDateObject
                .getJSONObject("miss_distance")
                .getDouble("astronomical")

            val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)

            asteroidList.add(asteroid)
        }
    }
    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}