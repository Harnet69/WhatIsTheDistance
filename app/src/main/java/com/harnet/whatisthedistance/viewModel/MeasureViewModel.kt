package com.harnet.whatisthedistance.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.StationKeyword
import com.harnet.whatisthedistance.model.StationsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MeasureViewModel(application: Application) : BaseViewModel(application) {
    private val stationsApiService = StationsApiService()
    private val disposable = CompositeDisposable()

    val mStations = MutableLiveData<ArrayList<Station>>()
    val mStationsKeywords = MutableLiveData<ArrayList<StationKeyword>>()
    val mIsLoading = MutableLiveData<Boolean>()
    val mErrorMsg = MutableLiveData<String>()

    fun refresh() {
        getStationsFromApi()
        getStationsKeywordsFromApi()
    }

    private fun retrieveStations(stationsList: ArrayList<Station>) {
        // set received list to observable mutable list
        mStations.postValue(stationsList)
        // switch off error message
        mErrorMsg.postValue(null)
        // switch off waiting spinner
        mIsLoading.postValue(false)
    }

    private fun retrieveStationsKeywords(stationsKeywordsList: ArrayList<StationKeyword>) {
        // set received list to observable mutable list
        mStationsKeywords.postValue(stationsKeywordsList)
        // switch off error message
        mErrorMsg.postValue(null)
        // switch off waiting spinner
        mIsLoading.postValue(false)
    }

    private fun getStationsFromApi() {
        mIsLoading.value = true

        disposable.add(
            stationsApiService.getStations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Station>>() {
                    override fun onSuccess(stationsList: List<Station>) {
                        //TODO implement storing in database every 24 hours
                        retrieveStations(stationsList as ArrayList<Station>)
//                        storeDogInDatabase(stationsList)
//                        SharedPreferencesHelper.invoke(getApplication()).saveTimeOfUpd(System.nanoTime())
                    }

                    // get an error
                    override fun onError(e: Throwable) {
                        mErrorMsg.value = e.localizedMessage
                        mIsLoading.value = false
                    }
                })
        )
    }

    private fun getStationsKeywordsFromApi() {
        mIsLoading.value = true

        disposable.add(
            stationsApiService.getStationsKeywords()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<StationKeyword>>() {
                    override fun onSuccess(stationsKeywordsList: List<StationKeyword>) {
                        //TODO implement storing in database every 24 hours
                        retrieveStationsKeywords(stationsKeywordsList as ArrayList<StationKeyword>)
//                        storeDogInDatabase(stationsList)
//                        SharedPreferencesHelper.invoke(getApplication()).saveTimeOfUpd(System.nanoTime())
                    }

                    // get an error
                    override fun onError(e: Throwable) {
                        mErrorMsg.value = e.localizedMessage
                        mIsLoading.value = false
                    }
                })
        )
    }

    fun isUserStationInStationsKeywords(userStationsName: String): Boolean? {
        return mStationsKeywords.value?.any { station -> station.keyword == userStationsName }
    }

    private fun getStationIdByKeyword(stationKeyword: String): Int? {
        val stations =
            mStationsKeywords.value?.filter { station -> station.keyword == stationKeyword }
        return stations?.get(0)?.station_id
    }

    private fun getStationCoords(stationId: Int): LatLng? {
        val station = mStations.value?.filter { it -> it.id == stationId }
        var statLat: Double? = null
        var statLon: Double? = null
        if (station != null && station.isNotEmpty()) {
            statLat = station[0].latitude
            statLon = station[0].longitude
        }
        if (statLat != null && statLon != null) {
            return LatLng(statLat, statLon)
        }
        return null
    }

    fun calculateDistance(depKeyword: String, arrKeyword: String): Double? {
        val depId = getStationIdByKeyword(depKeyword)
        val arrId = getStationIdByKeyword(arrKeyword)
        val depCoords = depId?.let { getStationCoords(it) }
        val arrCoords = arrId?.let { getStationCoords(it) }
        if (depCoords != null && arrCoords != null) {
            return SphericalUtil.computeDistanceBetween(depCoords, arrCoords) / 1000
        }
        return null
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}