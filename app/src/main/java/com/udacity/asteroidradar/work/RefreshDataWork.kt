package  com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.repository.AsteroidRepository
import com.udacity.asteroidradar.DataBase.getDatabasePic
import com.udacity.asteroidradar.DataBase.getDatabaseforAsteroid
import com.udacity.asteroidradar.repository.PicRepo

import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val asteroidDatabase = getDatabaseforAsteroid(applicationContext)
        val asteroidRepository = AsteroidRepository(asteroidDatabase)
        val picDatabase = getDatabasePic(applicationContext)
        val picRepository = PicRepo(picDatabase)
        return try {
            picRepository.refreshData()
            asteroidRepository.refreshData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}