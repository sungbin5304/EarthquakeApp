package com.sungbin.earthquakeapp.ui.dialog

/**
 * Created by SungBin on 2020-08-11.
 */

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.databinding.LayoutEarthquakeDetailBinding
import com.sungbin.earthquakeapp.model.EarthquakeData

class EarthquakeDetailBottomDialog constructor(val item: EarthquakeData) : BottomSheetDialogFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = DataBindingUtil.inflate<LayoutEarthquakeDetailBinding>(
            inflater,
            R.layout.layout_earthquake_detail,
            container,
            false
        )
        layout.item = item
        layout.invalidateAll()
        return layout.clContainer
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
                isHideable = true
            }
        }
        return bottomSheetDialog
    }
}