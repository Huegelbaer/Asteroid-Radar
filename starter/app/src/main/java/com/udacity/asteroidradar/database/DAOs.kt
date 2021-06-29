package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids")
    fun getAll(): List<DatabaseAsteroid>
}