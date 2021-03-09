package com.harnet.whatisthedistance.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.repository.StationRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationsListViewModel @ViewModelInject constructor(private val stationsRepository: StationRepositoryInterface) :
    ViewModel() {
    val mStations = MutableLiveData<List<Station>>()
    val mStationsLoadError = MutableLiveData<Boolean>()
    val mStationsLoading = MutableLiveData<Boolean>()

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
        return stationsList.sortedByDescending { it.id }.toCollection(ArrayList<Station>())
    }
}