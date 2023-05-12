package com.silverorange.videoplayer.data.repositories

import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.model.Video
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

enum class ApiMode {
    SUCCESS, ERROR, EXCEPTION
}

class FakeVideosApi : VideosApi {

    var mode: ApiMode = ApiMode.SUCCESS
    var videos: MutableList<Video> = mutableListOf()

    override suspend fun getVideos(): Response<List<Video>> {
        return when(mode) {
            ApiMode.SUCCESS -> Response.success(videos)
            ApiMode.ERROR -> {
                val body = ResponseBody.create(MediaType.parse("application/json"), "{}")
                Response.error(500, body)
            }
            ApiMode.EXCEPTION -> throw Exception()
        }
    }
}