package com.silverorange.videoplayer.utils

import android.widget.ImageButton
import android.widget.TextView
import io.noties.markwon.Markwon

fun ImageButton.setEnabledStatus(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) {
        1f
    } else {
        0.5f
    }
}

fun TextView.renderMarkdown(markdown: String) {
    val markwon = Markwon.create(context)
    markwon.setMarkdown(this, markdown)
}