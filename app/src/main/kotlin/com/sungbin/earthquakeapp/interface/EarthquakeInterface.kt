package com.sungbin.earthquakeapp.`interface`

import io.reactivex.rxjava3.core.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by SungBin on 2020-08-10.
 */

interface EarthquakeInterface {
    @GET("weather/earthquake_volcano/domesticlist.jsp?endSize=999.0&startLat=999.0&endLat=999.0&startLon=999.0&endLon=999.0&lat=999.0&lon=999.0&dist=999.0")
    fun getEarthquakeData(
        @Query("pNo") page: Int,
        @Query("startSize") type: String = "999.0",
        @Query("startTm") startDate: String = "2020-01-01",
        @Query("endTm") endDate: String = SimpleDateFormat(
            "yyyy-mm-dd",
            Locale.KOREA
        ).format(Date())
    ): Flowable<ResponseBody>
}