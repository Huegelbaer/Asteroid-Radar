package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Database(entities = [AsteroidsDatabase::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase() {

    abstract val asteroidDAO: AsteroidDAO
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}