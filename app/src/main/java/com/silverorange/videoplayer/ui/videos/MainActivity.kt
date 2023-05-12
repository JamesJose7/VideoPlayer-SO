package com.silverorange.videoplayer.ui.videos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.silverorange.videoplayer.data.api.RetrofitClient
import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.repositories.VideosRepository
import com.silverorange.videoplayer.databinding.ActivityMainBinding
import com.silverorange.videoplayer.ui.videos.model.VideosViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: VideosViewModel by lazy {
        val videosApi = RetrofitClient.getInstance().create(VideosApi::class.java)
        val videosRepository = VideosRepository(videosApi)
        VideosViewModel(videosRepository)
    }

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.getVideos()
        viewModel.currentVideo.observe(this) {
            it?.let { video ->
                Log.d(MainActivity::class.java.simpleName, "Current video: ${video.title}")
            }
        }

        loadVideosIntoPlayer()
    }

    private fun loadVideosIntoPlayer() {
        viewModel.getVideos()
        viewModel.videos.observe(this) {
            it?.let { videos ->
                val mediaItems = videos.map { video ->
                    MediaItem.Builder()
                        .setUri(video.hlsURL)
                        .setMediaId(video.id)
                        .build()
                }
                initializeVideoPlayer(mediaItems)
            }
        }
    }

    private fun initializeVideoPlayer(mediaItems: List<MediaItem>) {
        player = ExoPlayer.Builder(this)
            .build()
            .also { player ->
                binding.videoPlayer.player = player
            }

        mediaItems.forEach { player?.addMediaItem(it) }
        player?.prepare()
    }
}