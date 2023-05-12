package com.silverorange.videoplayer.data.api

import com.silverorange.videoplayer.data.model.Video
import retrofit2.Response
import retrofit2.http.GET

interface VideosApi {

    @GET("videos")
    suspend fun getVideos(): Response<List<Video>>
}