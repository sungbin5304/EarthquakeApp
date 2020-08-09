package com.sungbin.earthquakeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.`interface`.EarthquakeInterface
import com.sungbin.earthquakeapp.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var client: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..5) {
            client
                .create(EarthquakeInterface::class.java).run {
                    getEarthquakeData(page = i)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            LogUtils.log(
                                //Html.fromHtml(
                                    response
                                        ?.string()
                                        ?.split("<tbody>")
                                        ?.get(1)
                                        ?.split("tdpgt tgnlf pdl4")
                                        ?.get(0)
                                //).toString()
                            )
                        }, { throwable ->
                            LogUtils.log(throwable)
                        }, {
                            LogUtils.log("작업 끝")
                        })
                }
        }

        /*for (i in 1..10) {
            client
                .create(EarthquakeInterface::class.java).run {
                    getEarthquakeData(i, "20200810")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ jsonObject ->
                            LogUtils.json(jsonObject)
                        }, { throwable ->
                            LogUtils.log(throwable)
                        }, {
                            LogUtils.log("작업 끝")
                        })
                }
        }*/
    }
}