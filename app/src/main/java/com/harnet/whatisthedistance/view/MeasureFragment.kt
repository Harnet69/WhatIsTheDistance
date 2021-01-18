package com.harnet.whatisthedistance.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import com.harnet.whatisthedistance.R

class MeasureFragment : Fragment() {

    companion object {
        fun newInstance() = MeasureFragment()
    }

    private lateinit var viewModel: MeasureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.measure_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeasureViewModel::class.java)
        // TODO: Use the ViewModel
    }

}