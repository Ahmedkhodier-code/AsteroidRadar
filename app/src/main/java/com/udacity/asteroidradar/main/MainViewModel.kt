package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.example.android.devbyteviewer.repository.AsteroidRepository
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DataBase.getDatabaseforAsteroid
import com.udacity.asteroidradar.DataBase.getDatabasePic
import com.udacity.asteroidradar.repository.PicRepo
import kotlinx.coroutines.launch

enum class ApiFilter { TODAY, WEEKLY, SAVED }

class MainViewModel(application: Application) : ViewModel() {
    private val databaseForAsteroid = getDatabaseforAsteroid(application)
    private val asteroidRepository = AsteroidRepository(databaseForAsteroid)
    private val databaseForPic = getDatabasePic(application)
    private val pictureOfDayRepository = PicRepo(databaseForPic)

    private val _filter = MutableLiveData<ApiFilter>()

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    fun onAsteroidClicked(id: Asteroid) {
        _navigateToSelectedAsteroid.value = id
    }

    fun onAsteroidNavigated() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        _filter.value = ApiFilter.SAVED
        viewModelScope.launch {
            asteroidRepository.refreshData()
            pictureOfDayRepository.refreshData()
        }
    }

    val asteroidData = Transformations.switchMap(_filter) {
        when (it) {
            ApiFilter.TODAY -> asteroidRepository.todayAsteroid
            ApiFilter.WEEKLY -> asteroidRepository.weekAsteroid
            else ->asteroidRepository.asteroid
        }
    }
    val picData = pictureOfDayRepository.pic

    fun updateFilter(filter: ApiFilter) {
        _filter.value = filter
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

//    suspend fun updateFilter(start_date: ApiFilter, end_date: ApiFilter) {
//        asteroidRepository.refreshData(start_date, end_date)
//    }
}