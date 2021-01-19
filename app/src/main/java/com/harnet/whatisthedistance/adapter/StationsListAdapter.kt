package com.harnet.whatisthedistance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.databinding.ItemStationBinding
import com.harnet.whatisthedistance.model.Station

class StationsListAdapter(private val stationsList: ArrayList<Station>) :
    RecyclerView.Adapter<StationsListAdapter.StationsListViewHolder>() {

    //for updating information from a backend
    fun updateStationsList(newStationsList: ArrayList<Station>) {
        stationsList.clear()
        stationsList.addAll(newStationsList)
        //reset RecycleView and recreate a list
        notifyDataSetChanged()
    }

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
        return stationsList.size
    }

    class StationsListViewHolder(var view: ItemStationBinding) : RecyclerView.ViewHolder(view.root)

    override fun onBindViewHolder(holder: StationsListViewHolder, position: Int) {
        holder.view.station = stationsList[position]
    }
}