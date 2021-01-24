package com.sungbin.earthquakeapp.paging

import androidx.paging.PageKeyedDataSource
import com.sungbin.earthquakeapp.`interface`.EarthquakeInterface
import com.sungbin.earthquakeapp.di.DaggerEarthquakeComponent
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.utils.LogUtils
import com.sungbin.earthquakeapp.utils.ParsingUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by SungBin on 2020-08-01.
 */

class EarthquakeDataSource constructor(
    private var startDepth: Int,
    private var endDate: String,
    private var startDate: String
) : PageKeyedDataSource<Int, EarthquakeData>() {

    private var page = 1

    @Inject
    lateinit var client: Retrofit

    init {
        DaggerEarthquakeComponent.builder().build().inject(this)
    }

    private fun client(callback: (List<EarthquakeData>) -> Unit) {
        client
            .create(EarthquakeInterface::class.java).run {
                getEarthquakeData(
                    page,
                    if (startDepth > 0) "$startDepth.0" else "999.0",
                    startDate,
                    endDate
                )
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { response ->
                            callback(
                                ParsingUtils.get(
                                    page,
                                    response
                                        ?.string()
                                        ?.split("<tbody>")
                                        ?.get(1)
                                        ?.split("tdpgt tgnlf pdl4")
                                        ?.get(0)
                                        .toString()
                                )
                            )
                        },
                        { throwable ->
                            LogUtils.log(throwable)
                        },
                        {
                        }
                    )
            }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, EarthquakeData>
    ) {
        client {
            callback.onResult(it, 1, 2)
            LogUtils.log("init - $page")
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, EarthquakeData>
    ) {
        LogUtils.log("loadAfter")
        if (page + 1 > 1) {
            page++
            client {
                callback.onResult(it, page)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, EarthquakeData>
    ) {
        LogUtils.log("loadBefore")
        if (page - 1 > 0) {
            page--
            client {
                callback.onResult(it, page)
            }
        }
    }
}
