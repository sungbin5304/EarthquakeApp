package com.sungbin.earthquakeapp.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.`interface`.EarthquakeInterface
import com.sungbin.earthquakeapp.adapter.EarthquakeAdapter
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.ui.dialog.ProgressDialog
import com.sungbin.earthquakeapp.ui.dialog.SearchOptionBottomDialog
import com.sungbin.earthquakeapp.utils.ParsingUtils
import com.sungbin.earthquakeapp.utils.extension.setEndDrawableClickEvent
import com.sungbin.earthquakeapp.utils.manager.PathManager.END_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DEPTH
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var client: Retrofit

    private val loadingDialog by lazy {
        ProgressDialog(this)
    }

    private val viewModel by viewModels<MainViewModel>()

    private val bottomSheetDialog by lazy {
        SearchOptionBottomDialog.instance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_search.setEndDrawableClickEvent {
            bottomSheetDialog.show(supportFragmentManager, "검색 설정")
        }

        viewModel.items.observe(this, Observer<List<EarthquakeData>> {
            rv_earthquake.adapter = EarthquakeAdapter(it)
        })

        loadEarthquake()
        bottomSheetDialog.setSearchOptionDialogListener {
            loadEarthquake()
        }
    }

    private fun loadEarthquake() {
        client
            .create(EarthquakeInterface::class.java).run {

                var startDepth: Int
                var endDate: String
                var startDate: String

                defaultSharedPreferences.run {
                    startDepth = getInt(START_DEPTH, 999)
                    endDate = getString(
                        END_DATE, SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.KOREA
                        ).format(Date())
                    ).toString()
                    startDate = getString(START_DATE, "2020-01-01").toString()
                }

                loadingDialog.show()
                getEarthquakeData(
                    1,
                    if (startDepth > 0) "$startDepth.0" else "999.0",
                    startDate,
                    endDate
                )
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
                        loadingDialog.setError(throwable)
                    }, {
                        loadingDialog.close()

                    })
            }
    }
}