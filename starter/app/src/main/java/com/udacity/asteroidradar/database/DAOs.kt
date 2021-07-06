package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)

    @Query("SELECT * FROM asteroids ORDER BY asteroids.close_approach_date ASC")
    fun getAll(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY close_approach_date ASC")
    fun getAsteroidsInPeriod(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroids WHERE close_approach_date < :date")
    fun deleteAsteroidsBeforeDate(date: String)
}