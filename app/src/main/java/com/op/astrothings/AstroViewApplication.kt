package com.op.astrothings

import android.app.Application
import android.util.Log
import com.op.astrothings.BuildConfig
import com.op.astrothings.com.astrothings.util.CrashlyticsTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AstroViewApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initTimberLogging()
    }

    private fun initTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, msg: String, error: Throwable?) {
                    super.log(priority, "$tag", msg, error)
                    if (priority == Log.ERROR && tag != null && error != null) {
                    }
                }
            })
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

}