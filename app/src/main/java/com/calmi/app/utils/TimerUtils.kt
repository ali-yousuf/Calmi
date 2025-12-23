package com.calmi.app.utils

import java.util.Locale
import kotlin.time.Duration.Companion.seconds

/**
 * Formats a Long representing total seconds into a "MM:SS" string.
 */
fun Long.toMmSs(): String {
    if (this <= 0) return "00:00"
    val duration = this.seconds
    val minutes = duration.inWholeMinutes
    val seconds = (this % 60)
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}
