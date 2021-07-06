package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.database.AsteroidDAO
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
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
        var savedAsteroid = asteroids.value?.first()
        Assert.assertEquals(asteroid, savedAsteroid)

        val asteroid2 = DatabaseAsteroid(1, "1", "2021-01-03",
            0.0, 0.2, 2.6, 1.4,
            false)
        asteroidDao.insert(asteroid2)

        Assert.assertEquals(1.2, savedAsteroid!!.relativeVelocity, 0.0)

        asteroids = asteroidDao.getAll()
        savedAsteroid = asteroids.value?.first()

        Assert.assertEquals(2.6, savedAsteroid!!.relativeVelocity, 0.0)
    }

    @Test
    fun deleteOldAsteroids() {
        val asteroids: ArrayList<DatabaseAsteroid> = arrayListOf(
            DatabaseAsteroid(
                3, "30.06", "2021-06-30",
                0.0, 0.2, 1.2, 1.4,
                false
            ),
            DatabaseAsteroid(
                4, "01.07", "2021-07-01",
                0.0, 0.2, 1.2, 1.4,
                false
            ),
            DatabaseAsteroid(
                5, "02.07", "2021-07-02",
                0.0, 0.2, 1.2, 1.4,
                false
            ),
            DatabaseAsteroid(
                6, "03.07", "2021-07-03",
                0.0, 0.2, 1.2, 1.4,
                false
            ),
            DatabaseAsteroid(
                2, "01.06", "2021-06-01",
                0.0, 0.2, 1.2, 1.4,
                false
            )
        )
        asteroidDao.insertAll(asteroids)

        var savedAsteroids = asteroidDao.getAll()
        Assert.assertEquals(5, savedAsteroids.value?.count())

        asteroidDao.deleteAsteroidsBeforeDate("2021-07-02")

        savedAsteroids = asteroidDao.getAll()
        Assert.assertEquals(2, savedAsteroids.value?.count())
    }
}