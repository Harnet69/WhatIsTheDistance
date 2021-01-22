package com.harnet.whatisthedistance.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.retrofit.StationsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class StationsListViewModel(application: Application) : BaseViewModel(application) {
    private val stationsApiService = StationsApiService()
    private val disposable = CompositeDisposable()

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
        fetchFromRemoteWithRetrofit()

    }

    // fetches data with Retrofit library from remote API
    private fun fetchFromRemoteWithRetrofit() {
        mStationsLoading.value = true

        disposable.add(
            stationsApiService.getStations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Station>>() {
                    // get list of stations objects
                    override fun onSuccess(stationsList: List<Station>) {
                        //storing in database
                        retrieveStations(sortById(stationsList as ArrayList<Station>))
                    }

                    // get an error
                    override fun onError(e: Throwable) {
                        mStationsLoadError.value = true
                        mStationsLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    // sorted stations by it id
    private fun sortById(stationsList: ArrayList<Station>): ArrayList<Station> {
        return stationsList.sortedBy { it.id }.toCollection(ArrayList<Station>())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}