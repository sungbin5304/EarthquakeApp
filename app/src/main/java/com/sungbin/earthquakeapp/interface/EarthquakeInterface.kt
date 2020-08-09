package com.sungbin.earthquakeapp.`interface`

import com.google.gson.JsonObject
import com.sungbin.earthquakeapp.utils.manager.ApiManager.EarthquakeKey
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by SungBin on 2020-08-10.
 */

interface EarthquakeInterface {
    @GET("getTsunamiMsg?serviceKey=$EarthquakeKey&dataType=json&fromTmFc=20170101")
    fun getEarthquakeData(
        @Query("pageNo") index: Int,
        @Query("toTmFc") date: String
    ): Flowable<JsonObject>
}