package com.sungbin.earthquakeapp.ui.activity.main

import android.Manifest
import android.app.Service
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.adapter.EarthquakeAdapter
import com.sungbin.earthquakeapp.adapter.EarthquakePagingAdapter
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.paging.EarthquakeDataSource
import com.sungbin.earthquakeapp.ui.dialog.EarthquakeDetailBottomDialog
import com.sungbin.earthquakeapp.ui.dialog.SearchOptionBottomDialog
import com.sungbin.earthquakeapp.utils.LogUtils
import com.sungbin.earthquakeapp.utils.extension.setEndDrawableClickEvent
import com.sungbin.earthquakeapp.utils.manager.PathManager.END_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DEPTH
import com.sungbin.sungbintool.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val bottomSheetDialog by lazy {
        SearchOptionBottomDialog.instance()
    }

    private val imm by lazy {
        applicationContext.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val pagingAdapter by lazy {
        EarthquakePagingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtils.request(this, "", arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))

        et_search.setEndDrawableClickEvent {
            bottomSheetDialog.show(supportFragmentManager, "검색 설정")
        }

        pagingAdapter.setOnItemClickListener {
            LogUtils.log("-".repeat(50), it.mapImage, "-".repeat(50))
            EarthquakeDetailBottomDialog(it).show(supportFragmentManager, "")
        }

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
                    val searchedArray = pagingAdapter.currentList?.filter {
                        it.locate.contains(et_search.text.toString())
                    } ?: arrayListOf()
                    val searchedSet = hashSetOf<EarthquakeData>()
                    searchedSet.addAll(searchedArray)
                    rv_earthquake.adapter = EarthquakeAdapter(searchedSet.toList())
                    return@setOnEditorActionListener true
                }
                else -> {
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun loadEarthquake() {
        rv_earthquake.adapter = pagingAdapter

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        val liveData = initializedPagedListBuilder(config).build()

        liveData.observe(
            this@MainActivity,
            Observer<PagedList<EarthquakeData>> { pagedList ->
                pagingAdapter.submitList(pagedList)
            }
        )
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
        LivePagedListBuilder<Int, EarthquakeData> {
            val dataSourceFactory = object : DataSource.Factory<Int, EarthquakeData>() {
                override fun create(): DataSource<Int, EarthquakeData> {
                    var startDepth: Int
                    var endDate: String
                    var startDate: String

                    defaultSharedPreferences.run {
                        startDepth = getInt(START_DEPTH, 999)
                        endDate = getString(
                            END_DATE,
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.KOREA
                            ).format(Date())
                        ).toString()
                        startDate = getString(START_DATE, "2020-01-01").toString()
                    }

                    return EarthquakeDataSource(
                        startDepth,
                        endDate,
                        startDate
                    )
                }
            }
            return LivePagedListBuilder<Int, EarthquakeData>(dataSourceFactory, config)
        }
}
