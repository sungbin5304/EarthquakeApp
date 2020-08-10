package com.sungbin.earthquakeapp.ui.dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.edit
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.utils.extension.get
import com.sungbin.earthquakeapp.utils.extension.toEditable
import com.sungbin.earthquakeapp.utils.manager.PathManager.END_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DATE
import com.sungbin.earthquakeapp.utils.manager.PathManager.START_DEPTH
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class SearchOptionBottomDialog : BottomSheetDialogFragment() {

    private lateinit var etStartDepth: EditText
    private lateinit var etStartData: EditText
    private lateinit var etEndDate: EditText

    interface OnSearchOptionDialogListener {
        fun onClosed()
    }

    private var  listener: OnSearchOptionDialogListener? = null
    fun setOnDatabaseRemovedListener(listenerOn: OnSearchOptionDialogListener?) {
        this.listener = listenerOn
    }

    fun setSearchOptionDialogListener(listener: () -> Unit) {
        this.listener = object : OnSearchOptionDialogListener {
            override fun onClosed() {
                listener()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.layout_search_option, container, false)
        etStartDepth = layout[R.id.et_start_depth] as EditText
        etStartData = layout[R.id.et_start_date] as EditText
        etEndDate = layout[R.id.et_end_date] as EditText

        defaultSharedPreferences.run {
            etStartDepth.text = "${getInt(START_DEPTH, 0)}.0".toEditable()
            etStartData.text = getString(START_DATE, "2020-01-01").toEditable()
            etEndDate.text = getString(
                END_DATE, SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.KOREA
                ).format(Date())
            ).toEditable()
        }

        return layout
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        defaultSharedPreferences.edit {
            putString(END_DATE, etEndDate.text.toString())
            putString(START_DATE, etStartData.text.toString())
            putInt(START_DEPTH, etStartDepth.text.toString().toInt())
        }

        if(listener != null) listener!!.onClosed()
    }

    companion object {
        private val instance by lazy {
            SearchOptionBottomDialog()
        }

        fun instance() = instance
    }
}