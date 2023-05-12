package com.silverorange.videoplayer.ui.videos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.silverorange.videoplayer.data.api.RetrofitClient
import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.repositories.VideosRepository
import com.silverorange.videoplayer.ui.videos.model.VideosViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: VideosViewModel by lazy {
        val videosApi = RetrofitClient.getInstance().create(VideosApi::class.java)
        val videosRepository = VideosRepository(videosApi)
        VideosViewModel(videosRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getVideos()
        viewModel.currentVideo.observe(this) {
            it?.let { video ->
                Log.d(MainActivity::class.java.simpleName, "Current video: ${video.title}")
            }
        }
    }
}