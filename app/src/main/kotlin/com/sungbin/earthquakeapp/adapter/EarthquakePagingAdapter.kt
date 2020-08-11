package com.sungbin.earthquakeapp.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.earthquakeapp.R
import com.sungbin.earthquakeapp.databinding.LayoutEarthquakePanelBinding
import com.sungbin.earthquakeapp.model.EarthquakeData
import com.sungbin.earthquakeapp.paging.EarthquakeDiffUtilCallback


/**
 * Created by SungBin on 2020-07-20.
 */

class EarthquakePagingAdapter : PagedListAdapter<EarthquakeData, EarthquakePagingAdapter.ViewHolder>(
    EarthquakeDiffUtilCallback()
) {
    interface OnEarthquakeListener {
        fun onItemClicked(item: EarthquakeData)
    }

    private var listener: OnEarthquakeListener? = null
    fun setOnItemClickListener(action: (EarthquakeData) -> Unit) {
        listener = object : OnEarthquakeListener {
            override fun onItemClicked(item: EarthquakeData) {
                action(item)
            }
        }
    }

    class ViewHolder(private val itemBinding: LayoutEarthquakePanelBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindViewHolder(item: EarthquakeData) {
            itemBinding.item = item
        }
        fun bindOnClickListener(action: () -> Unit) {
            itemBinding.cvPanel.setOnClickListener {
                action()
            }
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
        getItem(position)?.let {
            viewholder.bindViewHolder(it)

            viewholder.bindOnClickListener {
                listener?.onItemClicked(it)
            }
        }
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

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position
}