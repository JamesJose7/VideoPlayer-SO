package com.silverorange.videoplayer.data.repositories

import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.model.Video

class VideosRepository(
    private val videosApi: VideosApi
) {

    suspend fun getVideos(): List<Video> {
        val response = videosApi.getVideos()
        return response.body() ?: emptyList()
    }
}