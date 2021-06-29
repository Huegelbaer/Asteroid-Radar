package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.database.AsteroidDAO
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.model.Asteroid
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseUnitTest {

    private lateinit var asteroidDao: AsteroidDAO
    private lateinit var db: AsteroidsDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, AsteroidsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        asteroidDao = db.asteroidDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAsteroid() {
        val asteroid = DatabaseAsteroid(1, "1", "2021-01-03",
            0.0, 0.2, 1.2, 1.4,
            false)

        asteroidDao.insert(asteroid)

        var asteroids = asteroidDao.getAll()
        var savedAsteroid = asteroids.first()
        Assert.assertEquals(asteroid, savedAsteroid)

        val asteroid2 = DatabaseAsteroid(1, "1", "2021-01-03",
            0.0, 0.2, 2.6, 1.4,
            false)
        asteroidDao.insert(asteroid)

        Assert.assertEquals(1.2, savedAsteroid.relativeVelocity, 0.0)

        asteroids = asteroidDao.getAll()
        savedAsteroid = asteroids.first()

        Assert.assertEquals(2.6, savedAsteroid.relativeVelocity, 0.0)
    }
}