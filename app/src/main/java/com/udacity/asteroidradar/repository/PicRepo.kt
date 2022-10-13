package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.DataBase.AsteroidDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PicRepo(private val database: AsteroidDatabase) {
    val pic: LiveData<PictureOfDay> =
        Transformations.map(database.pictureOfDayDao.getPictureOfDay()) { it }

    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            try {
                var Result = NasaApi.retrofitService.getPictureOfDay2()
                Log.i(
                    "onSuccess", "Success: ${Result.url} properties retrieved"
                )
                database.pictureOfDayDao.insertAll(Result)
            } catch (e: Exception) {
                e.message?.let { Log.d("Sample1", it) }
            }
        }
    }
}
