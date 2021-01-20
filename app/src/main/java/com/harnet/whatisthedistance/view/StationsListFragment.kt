package com.harnet.whatisthedistance.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.adapter.StationsListAdapter
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.viewModel.StationsListViewModel
import kotlinx.android.synthetic.main.stations_list_fragment.*

class StationsListFragment : Fragment() {
    private lateinit var viewModel: StationsListViewModel

    private lateinit var stationsListAdapter: StationsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stations_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationsListAdapter = StationsListAdapter(arrayListOf())

        viewModel = ViewModelProvider(this).get(StationsListViewModel::class.java)

        viewModel.refreshFromAPI()

        observeViewModel()

        stations_list_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stationsListAdapter
        }

        // add separation line between items
        stations_list_recyclerView.addItemDecoration(
            DividerItemDecoration(
                stations_list_recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeViewModel(){
        viewModel.mStations.observe(viewLifecycleOwner, Observer { stationsList ->
            // notify adapter
            stations_list_recyclerView.visibility = View.VISIBLE
            stationsList_progressBar.visibility = View.INVISIBLE
            stationsListAdapter.updateStationsList(stationsList as ArrayList<Station>)
        })

        viewModel.mStationsLoadError.observe(viewLifecycleOwner, Observer {
            if(it){
                stations_list_recyclerView.visibility = View.INVISIBLE
                stationsList_progressBar.visibility = View.INVISIBLE
                stationsList_errorMsg.visibility = View.VISIBLE
            }
        })
    }
}