package com.sungbin.earthquakeapp.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.databinding.LayoutEarthquakePanelBinding
import com.sungbin.earthquakeapp.model.EarthquakeData


/**
 * Created by SungBin on 2020-07-20.
 */

class EarthquakeAdapter constructor(
    private val items: List<EarthquakeData>
) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: LayoutEarthquakePanelBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(item: EarthquakeData) {
            itemBinding.item = item
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.layout_earthquake_panel, viewGroup, false
            )
        )

    override fun onBindViewHolder(@NonNull viewholder: ViewHolder, position: Int) {
        viewholder.bindViewHolder(items[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() { //아이템 간격
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
                    outRect.set(0, 0, 30, 0)
                }
            }
        })
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position
}