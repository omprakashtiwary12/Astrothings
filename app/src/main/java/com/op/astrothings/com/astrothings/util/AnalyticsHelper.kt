package com.op.astrothings.com.astrothings.util

import android.os.Bundle

interface AnalyticsHelper {

    fun logEvent(event: String, bundle: Bundle?)

}