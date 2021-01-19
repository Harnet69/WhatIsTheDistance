package com.harnet.whatisthedistance.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.model.StationKeyword
import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import kotlinx.android.synthetic.main.measure_fragment.*


class MeasureFragment : Fragment() {
    private lateinit var viewModel: MeasureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.measure_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeasureViewModel::class.java)

        stations_list_btn.setOnClickListener {
            Toast.makeText(context, "Go to stations", Toast.LENGTH_SHORT).show()
            val action = MeasureFragmentDirections.actionMeasureFragmentToStationsListFragment()
            Navigation.findNavController(view).navigate(action)
        }

        viewModel.refresh()

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.mStations.observe(viewLifecycleOwner, Observer { stationsList ->
            measure_progressBar.visibility = View.INVISIBLE
            Log.i("StationsList", "Stations: $stationsList")
        })

        viewModel.mStationsKeywords.observe(viewLifecycleOwner, Observer { stationsKeywordsList ->
            measure_progressBar.visibility = View.INVISIBLE
            Log.i("StationsList", "Stations keywords: ${stationsKeywordsList[0].keyword}")
            search_block_measureFragment.visibility = View.VISIBLE
            setToAutoComplete(dep_st, stationsKeywordsList)
            setToAutoComplete(arr_st, stationsKeywordsList)
        })

        viewModel.mErrorMsg.observe(viewLifecycleOwner, Observer { e ->
            if (e != null) {
                search_block_measureFragment.visibility = View.INVISIBLE
                measure_progressBar.visibility = View.INVISIBLE
                measure_error_msg.visibility = View.VISIBLE
                measure_error_msg.text = "Smth wrong $e"
            }
        })
    }

    private fun setToAutoComplete(autoCompleteTextView: AutoCompleteTextView, stationsKeywords: ArrayList<StationKeyword>){
        val stationsKeywordsList = arrayListOf<String>()
        for(station in stationsKeywords){
            stationsKeywordsList.add(station.keyword)
        }

        val arrayAdapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, stationsKeywordsList) }

        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.threshold = 1
    }
}