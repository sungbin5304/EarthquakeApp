package com.sungbin.earthquakeapp.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.utils.extension.plusAssign


class ProgressDialog constructor(val activity: Activity) {

    private lateinit var alert: AlertDialog
    private lateinit var layout: View

    @SuppressLint("InflateParams")
    fun show() {
        layout = LayoutInflater.from(activity).inflate(R.layout.layout_loading_dialog, null)
        val dialog = AlertDialog.Builder(activity)
        dialog.setView(layout)

        alert = dialog.create()
        alert.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    layout.context,
                    android.R.color.transparent
                )
            )
        )
        alert.show()
    }

    fun updateTitle(title: String) {
        layout.findViewById<TextView>(R .id.tv_loading) += title
        layout.invalidate()
    }

    fun setError(throwable: Throwable) {
        layout.findViewById<LottieAnimationView>(R.id.lav_load).run {
            setAnimation(R.raw.error)
            playAnimation()
        }
        layout.findViewById<TextView>(R.id.tv_loading).run {
            val message =
                "서버 요청 중 오류가 발생했습니다!\n\n${throwable.message} #${throwable.stackTrace[0].lineNumber}"
            val ssb = SpannableStringBuilder(message)
            ssb.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorLightRed)),
                message.lastIndexOf("\n"),
                message.lastIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = ssb
            movementMethod = ScrollingMovementMethod()
        }
        layout.invalidate()
    }

    fun close() {
        alert.cancel()
    }
}