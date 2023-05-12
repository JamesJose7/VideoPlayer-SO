package com.silverorange.videoplayer.data.repositories

import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.model.Result
import com.silverorange.videoplayer.data.model.Video
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX"

class VideosRepository(
    private val videosApi: VideosApi
) {

    suspend fun getVideos(): Result<List<Video>> {
        return try {
            val response = videosApi.getVideos()
            val videos = response.body()
            if (response.isSuccessful && videos != null) {
                Result.Success(sortVideosByDate(videos))
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
    private fun sortVideosByDate(videos: List<Video>): List<Video> =
        videos.sortedBy {
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            LocalDateTime.parse(it.publishedAt, formatter)
        }
}