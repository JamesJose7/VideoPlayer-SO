package com.silverorange.videoplayer.data.repositories

import com.silverorange.videoplayer.data.model.Author
import com.silverorange.videoplayer.data.model.Result
import com.silverorange.videoplayer.data.model.Video
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VideosRepositoryTest {

    private lateinit var videosApi: FakeVideosApi
    private lateinit var repository: VideosRepository

    @Before
    fun createRepository() {
        videosApi = FakeVideosApi()
        repository = VideosRepository(videosApi)
    }

    @Test
    fun receiveSuccessfulResponse_returnSuccessResult() = runTest {
        videosApi.mode = ApiMode.SUCCESS

        val response = repository.getVideos()

        assertTrue("Response was not a Success", response is Result.Success)
    }

    @Test
    fun receiveVideo_verifyVideoTitle() = runTest {
        val video = Video("", "Video title", "", "", "", "", Author("", ""))
        videosApi.mode = ApiMode.SUCCESS
        videosApi.videos = mutableListOf(video)

        val response = repository.getVideos()

        response as Result.Success
        assertEquals(video.title, response.data.first().title)
    }

    @Test
    fun receiveErrorResponse_returnErrorResult() = runTest {
        videosApi.mode = ApiMode.ERROR

        val response = repository.getVideos()

        assertTrue("Response was not an Error", response is Result.Error)
    }

    @Test
    fun receiveFailureResponse_returnFailureResult() = runTest {
        videosApi.mode = ApiMode.EXCEPTION

        val response = repository.getVideos()

        assertTrue("Response was not a Failure", response is Result.Failure)
    }
}

