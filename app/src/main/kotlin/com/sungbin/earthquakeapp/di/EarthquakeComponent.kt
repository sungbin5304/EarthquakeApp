package com.sungbin.earthquakeapp.di

import com.sungbin.earthquakeapp.module.EarthquakeModule
import com.sungbin.earthquakeapp.paging.EarthquakeDataSource
import dagger.Component
import javax.inject.Singleton


/**
 * Created by SungBin on 2020-08-11.
 */

@Singleton
@Component(modules = [EarthquakeModule::class])
interface EarthquakeComponent {
    fun inject(activity: EarthquakeDataSource)
}