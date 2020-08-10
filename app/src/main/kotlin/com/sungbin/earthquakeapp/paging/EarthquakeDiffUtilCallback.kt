package com.sungbin.earthquakeapp.paging

import androidx.recyclerview.widget.DiffUtil
import com.sungbin.earthquakeapp.model.EarthquakeData


/**
 * Created by SungBin on 2020-08-01.
 */

class EarthquakeDiffUtilCallback : DiffUtil.ItemCallback<EarthquakeData>() {
    override fun areItemsTheSame(oldItem: EarthquakeData, newItem: EarthquakeData) =
        oldItem.page == newItem.page

    override fun areContentsTheSame(oldItem: EarthquakeData, newItem: EarthquakeData) =
        oldItem.depth == newItem.depth
                && oldItem.intensity == newItem.intensity
                && oldItem.latitude == newItem.latitude
                && oldItem.locate == newItem.locate
                && oldItem.longitude == newItem.longitude
                && oldItem.magnitude == newItem.magnitude
                && oldItem.page == newItem.page
                && oldItem.mapImage == newItem.mapImage
                && oldItem.time == newItem.time
}
