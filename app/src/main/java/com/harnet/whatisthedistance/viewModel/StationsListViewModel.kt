package com.harnet.whatisthedistance.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.harnet.whatisthedistance.repository.StationsRepository
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.di.DaggerStationsRepositoryComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StationsListViewModel(application: Application) : BaseViewModel(application) {
    val mStations = MutableLiveData<List<Station>>()
    val mStationsLoadError = MutableLiveData<Boolean>()
    val mStationsLoading = MutableLiveData<Boolean>()

    @Inject
    lateinit var stationsRepository: StationsRepository

    init {
        DaggerStationsRepositoryComponent.create().inject(this)
    }

    // retrieve stations and set UI components
    private fun retrieveStations(stationsList: List<Station>) {
        mStations.postValue(stationsList)
        mStationsLoadError.postValue(false)
        mStationsLoading.postValue(false)
    }

    //refresh information from remote API
    fun refreshFromAPI() {
        launch {
            val stList = stationsRepository.getAllStations(getApplication())
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