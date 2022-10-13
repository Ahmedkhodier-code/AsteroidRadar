package com.udacity.asteroidradar.DataBase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

//enum class ApiFilter(val value: String) { START_DATE(""), END_DATE("") }

@Dao
interface AsteroidDao {
    @Query("select * from Asteroid order by closeApproachDate asc")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("select * from Asteroid where closeApproachDate=:date")
    fun getAsteroidsOfDay(date: String): LiveData<List<Asteroid>>

    @Query("select * from Asteroid where closeApproachDate between :start and :end order by closeApproachDate asc")
    fun getAsteroidsOfWeek(start: String, end: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)
}

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 3)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabaseforAsteroid(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "Asteroid"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

@Dao
interface PictureOfDayDao {
    @Query("select * from PictureOfDay")
    fun getPictureOfDay(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pictureOfDay: PictureOfDay)
}

private lateinit var INSTANCE2: AsteroidDatabase

fun getDatabasePic(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE2.isInitialized) {
            INSTANCE2 = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "PictureOfDay"
            ).build()
        }
    }
    return INSTANCE2
}