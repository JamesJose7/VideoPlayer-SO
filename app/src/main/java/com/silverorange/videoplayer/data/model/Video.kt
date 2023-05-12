package com.silverorange.videoplayer.data.model

data class Video(
    private val id: String,
    private val title: String,
    private val hlsURL: String,
    private val fullURL: String,
    private val description: String,
    private val publishedAt: String,
    private val author: Author
)
