package com.example.android.devbyteviewer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.DataBase.AsteroidDatabase
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {
    var today: String = ""
    var endDay: String = ""

    init {
        val calendar = Calendar.getInstance()
        today = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault()).format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        endDay = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault()).format(calendar.time)
    }

    val asteroid: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }
    val todayAsteroid: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsOfDay(today)) {
            it.asDomainModel()
        }
    val weekAsteroid: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsOfWeek(today, endDay)) {
            it.asDomainModel()
        }

    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            try {
                Timber.tag("Test Log").i("iam in try")
                val response = NasaApi.retrofitService.getAsteroid()
                val Js = JSONObject(response)
                val Result = parseAsteroidsJsonResult(Js)
                Timber.tag("Sample").d("response: " + response)
                database.asteroidDao.insertAll(*Result.asDatabaseModel())
            } catch (e: Exception) {
                e.message?.let { Log.d("Sample1", it) }
            }
        }
    }
}
