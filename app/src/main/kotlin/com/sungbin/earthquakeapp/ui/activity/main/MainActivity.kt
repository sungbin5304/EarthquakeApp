package com.sungbin.earthquakeapp.ui.activity.main

import android.Manifest
import android.app.Service
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.`interface`.EarthquakeInterface
import com.sungbin.earthquakeapp.adapter.EarthquakeAdapter
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.ui.dialog.ProgressDialog
import com.sungbin.earthquakeapp.ui.dialog.SearchOptionBottomDialog
import com.sungbin.earthquakeapp.utils.LogUtils
import com.sungbin.earthquakeapp.utils.extension.setEndDrawableClickEvent
import com.sungbin.earthquakeapp.utils.manager.PathManager.END_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DEPTH
import com.sungbin.sungbintool.PermissionUtils
import com.sungbin.sungbintool.StorageUtils
import com.sungbin.sungbintool.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Retrofit
import java.net.URLEncoder
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

    private val imm by lazy {
        applicationContext.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.request(this, "", arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))

        et_search.setEndDrawableClickEvent {
            bottomSheetDialog.show(supportFragmentManager, "검색 설정")
        }

        viewModel.items.observe(this, Observer<List<EarthquakeData>> {
            rv_earthquake.adapter = EarthquakeAdapter(it)
            //LogUtils.log(it)
        })

        loadEarthquake()
        bottomSheetDialog.setSearchOptionDialogListener {
            loadEarthquake()
        }

        et_search.imeOptions = EditorInfo.IME_ACTION_SEARCH
        et_search.setOnEditorActionListener { _, actionId, _ ->
            imm.hideSoftInputFromWindow(
                et_search.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    loadEarthquake()
                    return@setOnEditorActionListener true
                }
                else -> {
                    return@setOnEditorActionListener false
                }
            }
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

                LogUtils.log(et_search.text, URLEncoder.encode(et_search.text.toString(), "EUC-KR"))

                loadingDialog.show()
                getEarthquakeData(
                    URLEncoder.encode(et_search.text.toString(), "EUC-KR"),
                    1,
                    if (startDepth > 0) "$startDepth.0" else "999.0",
                    startDate,
                    endDate
                )
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        val html = response.string()

                        StorageUtils.save("html.txt", html)
                        Utils.copy(applicationContext, html.split("<tbody>")[1])

                        /*//Utils.copy(applicationContext, response?.string()!!)
                        viewModel.items.value =
                            ParsingUtils.get(
                                response
                                    ?.string()
                                    ?.split("<tbody>")
                                    ?.get(1)
                                    *//*?.split("tdpgt tgnlf pdl4")
                                    ?.get(0)*//*
                                    .toString()
                            )
                        viewModel.items.value.let {
                            LogUtils.log("호출댐", it?.size)
                        }*/
                    }, { throwable ->
                        loadingDialog.setError(throwable)
                        throwable.printStackTrace()
                    }, {
                        loadingDialog.close()
                    })
            }
    }
}