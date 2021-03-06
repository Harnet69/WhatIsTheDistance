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
import javax.inject.Inject

class StationsListFragment @Inject constructor(private val stationsListAdapter: StationsListAdapter) : Fragment() {
    private lateinit var viewModel: StationsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stations_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(StationsListViewModel::class.java)

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

        // Swiper refresh listener(screen refreshing process)
        refreshLayout_stationsList.setOnRefreshListener {
            stationsList_errorMsg.visibility = View.GONE
            stationsList_progressBar.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            refreshLayout_stationsList.isRefreshing = false // disappears little spinner on the top

        }
    }

    private fun observeViewModel(){
        viewModel.mStations.observe(viewLifecycleOwner, Observer { stationsList ->
            // notify adapter
            if (!stationsList.isNullOrEmpty()) {
                header_statiosList.visibility = View.VISIBLE
                stations_list_recyclerView.visibility = View.VISIBLE
                stationsList_progressBar.visibility = View.INVISIBLE
                stationsListAdapter.stations = (stationsList as ArrayList<Station>)
            }
        })

        viewModel.mStationsLoadError.observe(viewLifecycleOwner, Observer {
            if(it){
                header_statiosList.visibility = View.INVISIBLE
                stations_list_recyclerView.visibility = View.INVISIBLE
                stationsList_progressBar.visibility = View.INVISIBLE
                stationsList_errorMsg.visibility = View.VISIBLE
            }
        })
    }
}