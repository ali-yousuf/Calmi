package com.calmi.app.utils


import kotlin.time.Duration.Companion.seconds

/**
 * Formats a Long representing total seconds into a "MM:SS" string.
 */
fun Long.toMmSs(): String {
    if (this <= 0) return "00:00"
    val duration = this.seconds
    val minutes = duration.inWholeMinutes
    val seconds = (this % 60)
    return String.format(Constants.TIME_FORMAT_LOCALE, Constants.TIME_FORMAT_MM_SS, minutes, seconds)
}
