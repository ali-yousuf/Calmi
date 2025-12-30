package com.calmi.app.utils


import java.util.Locale
import kotlin.time.Duration.Companion.minutes

object Constants {
    val DEFAULT_TIMER_DURATION = 30.minutes
    const val TIME_FORMAT_MM_SS = "%02d:%02d"
    val TIME_FORMAT_LOCALE: Locale = Locale.US
}