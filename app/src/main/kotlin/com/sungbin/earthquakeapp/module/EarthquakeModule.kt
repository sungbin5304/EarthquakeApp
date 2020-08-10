package com.sungbin.earthquakeapp.module

import com.sungbin.earthquakeapp.utils.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import javax.inject.Singleton


/**
 * Created by SungBin on 2020-08-10.
 */

@Module
@InstallIn(ApplicationComponent::class)
class EarthquakeModule {

    @Singleton
    @Provides
    fun instance() = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(ApiManager.BASE_URI)
        .build()
}