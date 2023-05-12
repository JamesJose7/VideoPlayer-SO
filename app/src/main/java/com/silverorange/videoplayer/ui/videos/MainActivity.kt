package com.silverorange.videoplayer.ui.videos

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.silverorange.videoplayer.R
import com.silverorange.videoplayer.data.api.RetrofitClient
import com.silverorange.videoplayer.data.api.VideosApi
import com.silverorange.videoplayer.data.repositories.VideosRepository
import com.silverorange.videoplayer.databinding.ActivityMainBinding
import com.silverorange.videoplayer.ui.videos.model.VideosViewModel
import com.silverorange.videoplayer.utils.setEnabledStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: VideosViewModel by lazy {
        val videosApi = RetrofitClient.getInstance().create(VideosApi::class.java)
        val videosRepository = VideosRepository(videosApi)
        VideosViewModel(videosRepository)
    }

    private var player: ExoPlayer? = null

    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bindPlayerButtons()

        loadVideosIntoPlayer()
        loadCurrentVideoContent()
    }

    override fun onPause() {
        super.onPause()
        pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }

    private fun bindPlayerButtons() = with(binding.videoPlayer) {
        playButton = findViewById(R.id.play)
        pauseButton = findViewById(R.id.pause)
        nextButton = findViewById(R.id.next)
        previousButton = findViewById(R.id.prev)
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
                initializePlayerControls()
                pauseVideo()
                updateNextAndPreviousButtonStates()
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
        player?.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                viewModel.updateCurrentVideo(mediaItem?.mediaId)
                updateNextAndPreviousButtonStates()
            }
        })
        player?.prepare()
    }

    private fun initializePlayerControls() {
        playButton.setOnClickListener { playVideo() }
        pauseButton.setOnClickListener { pauseVideo() }
        nextButton.setOnClickListener { nextVideo() }
        previousButton.setOnClickListener { previousVideo() }
    }

    private fun playVideo() {
        player?.play()
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
    }

    private fun pauseVideo() {
        player?.pause()
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    private fun nextVideo() {
        player?.seekToNext()
    }

    private fun previousVideo() {
        player?.seekToPreviousMediaItem()
    }

    private fun updateNextAndPreviousButtonStates() {
        val isPlaylistAtBeginning = player?.hasPreviousMediaItem() == false
        val isPlaylistAtEnd = player?.hasNextMediaItem() == false
        previousButton.setEnabledStatus(!isPlaylistAtBeginning)
        nextButton.setEnabledStatus(!isPlaylistAtEnd)
    }

    private fun loadCurrentVideoContent() {
        viewModel.currentVideo.observe(this) {
            it?.let { currentVideo ->
                binding.videoTitle.text = currentVideo.title
                binding.videoAuthor.text = currentVideo.author.name
                binding.videoDescription.text = currentVideo.description
            }
        }
    }
}