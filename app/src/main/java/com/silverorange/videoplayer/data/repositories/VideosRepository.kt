package com.silverorange.videoplayer.data.repositories

import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.model.Result
import com.silverorange.videoplayer.data.model.Video

class VideosRepository(
    private val videosApi: VideosApi
) {

    suspend fun getVideos(): Result<List<Video>> {
        return try {
            val response = videosApi.getVideos()
            val videos = response.body()
            if (response.isSuccessful && videos != null) {
                Result.Success(videos)
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}