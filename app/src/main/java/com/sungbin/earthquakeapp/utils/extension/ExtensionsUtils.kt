package com.sungbin.earthquakeapp.utils.extension

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes
import com.google.gson.JsonObject
import com.sungbin.earthquakeapp.EarthquakeApp
import com.sungbin.sungbintool.StringUtils
import com.sungbin.sungbintool.ToastUtils


/**
 * Created by SungBin on 2020-07-28.
 */

fun toast(message: Any?, duration: Int = ToastUtils.SHORT, type: Int = ToastUtils.SUCCESS) {
    ToastUtils.show(EarthquakeApp.context, message.toString(), duration, type)
}

fun View.hide(isGone: Boolean = false){
    this.visibility = if (isGone) View.GONE else View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

operator fun TextView.plusAssign(text: String) {
    this.text = text
}

fun TextView.clear() {
    this.text = ""
}

operator fun View.get(@IdRes id: Int) = this.findViewById<View>(id)!!

fun JsonObject.getString(keyword: String) = this[keyword].toString().replace("\"", "")

fun String?.toEditable() = StringUtils.toEditable(this.toString())