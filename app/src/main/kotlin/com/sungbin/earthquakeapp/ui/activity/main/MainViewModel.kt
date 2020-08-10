package com.sungbin.earthquakeapp.ui.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.earthquakeapp.model.EarthquakeData


/**
 * Created by SungBin on 2020-08-10.
 */

class MainViewModel : ViewModel() {

    val items: MutableLiveData<List<EarthquakeData>> = MutableLiveData()

}