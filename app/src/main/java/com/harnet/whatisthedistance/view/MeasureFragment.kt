package com.harnet.whatisthedistance.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.model.StationKeyword
import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import kotlinx.android.synthetic.main.measure_fragment.*
import java.io.IOException


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

        showAboutDialog()

        viewModel.refresh()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.mStations.observe(viewLifecycleOwner, { stationsList ->
            measure_progressBar.visibility = View.INVISIBLE
        })

        viewModel.mStationsKeywords.observe(viewLifecycleOwner, { stationsKeywordsList ->
            measure_progressBar.visibility = View.INVISIBLE
            search_block_measureFragment.visibility = View.VISIBLE
            Log.i("landscapeCrash", "observeViewModel: ${stationsKeywordsList.size}")
            setToAutoComplete(dep_st, stationsKeywordsList)
            setToAutoComplete(arr_st, stationsKeywordsList)

            addDistanceBtn()
        })

        viewModel.mErrorMsg.observe(viewLifecycleOwner, { e ->
            if (e != null) {
                search_block_measureFragment.visibility = View.INVISIBLE
                measure_progressBar.visibility = View.INVISIBLE
                measure_error_msg.visibility = View.VISIBLE
                measure_error_msg.text = "Smth wrong $e"
            }
        })

        viewModel.mIsInternet.observeForever { isInternet ->
            measure_isInternet?.let {
                isInternet?.let {internet ->
                    if (internet) {
                        it.visibility = View.INVISIBLE
                        Log.i("isInternet", "Internet")
                    } else {
                        it.visibility = View.VISIBLE
                        measure_progressBar.visibility = View.INVISIBLE
                        Log.i("isInternet", "No Internet")
                    }
                }
            }
        }
    }

    private fun setToAutoComplete(
        autoCompleteTextView: AutoCompleteTextView,
        stationsKeywords: ArrayList<StationKeyword>
    ) {
        val stationsKeywordsList = arrayListOf<String>()
        for (station in stationsKeywords) {
            stationsKeywordsList.add(station.keyword)
        }

        val arrayAdapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                stationsKeywordsList
            )
        }
        Log.i("landscapeCrash", "setToAutoComplete: ${stationsKeywords.size}")
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.threshold = 1
    }

    private fun addDistanceBtn() {
        calc_btn.setOnClickListener {
            it.visibility = View.VISIBLE
            //clear the distance field
            distance_res.text = ""
            if (dep_st.text.toString() != "" && arr_st.text.toString() != "") {
                if (viewModel.isUserStationInStationsKeywords(dep_st.text.toString()) == true) {
                    if (viewModel.isUserStationInStationsKeywords(arr_st.text.toString()) == true) {
                        // calculate distance
                        val distance = viewModel.calculateDistance(
                            dep_st.text.toString(),
                            arr_st.text.toString()
                        )
                        if (distance != null) {
                            distance_res.text =
                                viewModel.roundOffDecimal(distance).toString() + " km"
                        } else {
                            Toast.makeText(
                                context,
                                "No coordinates yet for this place",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Place ${arr_st.text.toString()} is unknown",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Place ${dep_st.text.toString()} is unknown",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "Fill both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // show About app dialog window
    private fun showAboutDialog() {
        if (!viewModel.getIsAboutShowed()!!) {
            AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_distance)
                .setTitle(R.string.app_about_title)
                .setMessage(R.string.app_about_text)
                .setPositiveButton(R.string.app_about_agree) { dialogInterface: DialogInterface, i: Int ->
                    try {
                        viewModel.setIsAboutShowed(true)
                        // refresh the fragment
                        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                        if (Build.VERSION.SDK_INT >= 26) {
                            ft.setReorderingAllowed(false)
                        }
                        ft.detach(this).attach(this).commit()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.show()
        }
    }
}