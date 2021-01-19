package com.harnet.whatisthedistance.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.viewModel.StationsListViewModel
import kotlinx.android.synthetic.main.stations_list_fragment.*

class StationsListFragment : Fragment() {
    private lateinit var viewModel: StationsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stations_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StationsListViewModel::class.java)

        viewModel.refreshFromAPI()

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.mStations.observe(viewLifecycleOwner, Observer { stationsList ->
            // notify adapter
            stations_list_recyclerView.visibility = View.VISIBLE
            stationsList_progressBar.visibility = View.INVISIBLE
            Log.i("StationsList", "StationsList arrived: $stationsList")
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