package com.silverorange.videoplayer.utils

import android.widget.ImageButton

fun ImageButton.setEnabledStatus(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) {
        1f
    } else {
        0.5f
    }
}