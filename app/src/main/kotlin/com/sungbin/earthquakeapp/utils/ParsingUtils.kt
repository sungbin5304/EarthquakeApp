package com.sungbin.earthquakeapp.utils

import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.utils.extension.parse

object ParsingUtils {

    fun get(page: Int , htmlData: String): ArrayList<EarthquakeData> {
        val totalData = htmlData.run {
            substring(0..lastIndexOf("<tr>"))
        }
        val earthquakeTotalData = ArrayList<EarthquakeData>()
        for (i in 1 until totalData.split("<tr>").size) {
            val data = totalData.split("<tr>")[i].split("</tr>")[0]
            val time = data.parse("td", index = 2)
            val intensity = data.parse("td", index = 3)
            val depth = data.parse("td", index = 4)
            val magnitude = data.parse("td", index = 5)
            val latitude = data.parse("td", index = 6)
            val longitude = data.parse("td", index = 7)
            val locate = data.parse("<td", "</td>", 8).split(">")[1].run {
                val slice = split(" ")
                val first = "${slice[0]} ${slice[1]}"
                val second = toString().split(first)[1]
                first + "\n" + second
            }
            val mapImage = data.parse("td", index = 9)
            val earthquakeData = EarthquakeData(
                page,
                time,
                intensity,
                depth,
                magnitude,
                latitude,
                longitude,
                locate,
                mapImage
            )
            earthquakeTotalData.add(earthquakeData)
        }
        return earthquakeTotalData
    }

}