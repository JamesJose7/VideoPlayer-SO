package com.silverorange.videoplayer.ui.videos.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.data.repositories.VideosRepository
import kotlinx.coroutines.launch

class VideosViewModel(
    private val videosRepository: VideosRepository
) : ViewModel() {

    fun getVideos() {
        viewModelScope.launch {
            val videos = videosRepository.getVideos()
            Log.d(VideosViewModel::class.java.simpleName, videos.toString())
        }
    }
}