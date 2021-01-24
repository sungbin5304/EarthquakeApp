package com.sungbin.earthquakeapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by SungBin on 2020-08-10.
 */

@HiltAndroidApp
class EarthquakeApp : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
