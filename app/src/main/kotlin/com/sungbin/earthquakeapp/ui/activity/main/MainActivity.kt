package com.sungbin.earthquakeapp.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.`interface`.EarthquakeInterface
import com.sungbin.earthquakeapp.adapter.EarthquakeAdapter
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.utils.LogUtils
import com.sungbin.earthquakeapp.utils.ParsingUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var client: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel by viewModels<MainViewModel>()

        viewModel.items.observe(this, Observer<List<EarthquakeData>> {
            rv_earthquake.adapter = EarthquakeAdapter(it)
        })

        client
            .create(EarthquakeInterface::class.java).run {
                getEarthquakeData(page = 1)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        viewModel.items.value =
                            ParsingUtils.get(
                                response
                                    ?.string()
                                    ?.split("<tbody>")
                                    ?.get(1)
                                    ?.split("tdpgt tgnlf pdl4")
                                    ?.get(0)
                                    .toString()
                            )
                    }, { throwable ->
                        LogUtils.log(throwable)
                    }, {
                        LogUtils.log("작업 끝"/*, items.value?.random().toString()*/)
                    })
            }
    }
}