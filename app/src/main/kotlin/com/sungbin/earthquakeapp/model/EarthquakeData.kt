package com.sungbin.earthquakeapp.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sungbin.earthquakeapp.module.GlideApp


/**
 * Created by SungBin on 2020-08-10.
 */

data class EarthquakeData(
    val page: Int,
    val time: String,
    val intensity: String,
    val depth: String,
    val magnitude: String,
    val latitude: String,
    val longitude: String,
    val locate: String,
    val mapImage: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(imageView: ImageView, url: String?) {
            GlideApp
                .with(imageView.context)
                .load(url ?: "")
                .into(imageView)
        }
    }

    override fun toString(): String {
        return """
            time : $time
            intensity : $intensity
            depth : $depth
            magnitude : $magnitude
            latitude : $latitude
            longitude : $longitude
            locate : $locate
            mapImage : $mapImage
        """.trimIndent()
    }
}