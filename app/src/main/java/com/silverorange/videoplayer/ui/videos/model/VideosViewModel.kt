package com.silverorange.videoplayer.ui.videos.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.data.model.Result
import com.silverorange.videoplayer.data.model.Video
import com.silverorange.videoplayer.data.repositories.VideosRepository
import kotlinx.coroutines.launch

class VideosViewModel(
    private val videosRepository: VideosRepository
) : ViewModel() {

    private val TAG = VideosViewModel::class.java.simpleName

    private var _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>>
        get() = _videos

    private var _currentVideo = MutableLiveData<Video>()
    val currentVideo: LiveData<Video>
        get() = _currentVideo

    fun getVideos() {
        viewModelScope.launch {
            when (val response = videosRepository.getVideos()) {
                is Result.Success -> loadVideos(response.data)
                is Result.Error -> Log.e(TAG, "Code: ${response.code} - Message: ${response.message}")
                is Result.Failure -> Log.e(TAG, response.e.message, response.e)
            }
        }
    }
    private fun loadVideos(videos: List<Video>) {
        _videos.value = videos
        if (videos.isNotEmpty()) {
            _currentVideo.value = videos.first()
        }
    }
}