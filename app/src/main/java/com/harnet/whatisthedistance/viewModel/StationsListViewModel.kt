package com.harnet.whatisthedistance.viewModel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.repository.StationRepositoryInterface
import com.harnet.whatisthedistance.repository.StationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StationsListViewModel @ViewModelInject constructor(private val stationsRepository: StationRepositoryInterface)  : ViewModel() {
    val mStations = MutableLiveData<List<Station>>()
    val mStationsLoadError = MutableLiveData<Boolean>()
    val mStationsLoading = MutableLiveData<Boolean>()

//    val stationsRepository: StationsRepository = StationsRepository()

    // retrieve stations and set UI components
    private fun retrieveStations(stationsList: List<Station>) {
        mStations.postValue(stationsList)
        mStationsLoadError.postValue(false)
        mStationsLoading.postValue(false)
    }

    //refresh information from remote API
    fun refreshFromAPI() {
        viewModelScope.launch {
            val stList = stationsRepository.getAllStations()
            launch(Dispatchers.Main) {
                retrieveStations(sortById(stList as ArrayList<Station>))
            }
        }
    }

    // sorted stations by it id
    private fun sortById(stationsList: ArrayList<Station>): ArrayList<Station> {
        return stationsList.sortedBy { it.id }.toCollection(ArrayList<Station>())
    }
}