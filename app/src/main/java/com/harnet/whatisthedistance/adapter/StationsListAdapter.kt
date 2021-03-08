package com.harnet.whatisthedistance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.databinding.ItemStationBinding
import com.harnet.whatisthedistance.model.Station
import javax.inject.Inject

class StationsListAdapter @Inject constructor(
    private val glide: RequestManager
):
    RecyclerView.Adapter<StationsListAdapter.StationsListViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Station>(){
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerViewDiffer = AsyncListDiffer(this, diffUtil)

    var stations: List<Station>
        get() = recyclerViewDiffer.currentList
        set(value) = recyclerViewDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // elements of the list transforms into views. DataBinding approach
        val view = DataBindingUtil.inflate<ItemStationBinding>(
            inflater,
            R.layout.item_station,
            parent,
            false
        )
        return StationsListAdapter.StationsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    class StationsListViewHolder(var view: ItemStationBinding) : RecyclerView.ViewHolder(view.root)

    override fun onBindViewHolder(holder: StationsListViewHolder, position: Int) {
        holder.view.station = stations[position]
    }
}