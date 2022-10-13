package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@JsonClass(generateAdapter = true)
data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String, val title: String,
    @PrimaryKey
    val url: String
) : Parcelable

fun List<PictureOfDay>.asDomainModel(): List<PictureOfDay> {
    return map {
        PictureOfDay(
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }
}