package com.harnet.whatisthedistance.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.harnet.dogbreeds.model.StationsDatabase
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.StationKeyword
import com.harnet.whatisthedistance.model.StationsApiService
import com.harnet.whatisthedistance.model.StationsKeywordsDatabase
import com.harnet.whatisthedistance.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MeasureViewModel(application: Application) : BaseViewModel(application) {
    private var updateTime: Long = 1L

    // helper for SharedPreferences functionality
    private var sharedPrefHelper = SharedPreferencesHelper(getApplication())

    private val stationsApiService = StationsApiService()
    private val disposable = CompositeDisposable()

    val mStations = MutableLiveData<ArrayList<Station>>()
    val mStationsKeywords = MutableLiveData<ArrayList<StationKeyword>>()
    val mIsLoading = MutableLiveData<Boolean>()
    val mErrorMsg = MutableLiveData<String>()

    // TODO when the time to update from API
    fun refreshFromApi() {
        getDataFromAPIs()
    }

    // TODO when lost less when 24 hours
    fun refreshFromDb() {
        getDataFromAPIs()
    }

    private fun getDataFromAPIs(){
        getStationsFromApi()
        getStationsKeywordsFromApi()
    }

    private fun getDataFromDbs(){
        fetchStationsFromDatabase()
        fetchStationsKeywordsFromDatabase()
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
                        storeStationsInDatabase(stationsList)
                        SharedPreferencesHelper.invoke(getApplication()).saveTimeOfUpd(System.nanoTime())
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
                        storeStationsKeywordsInDatabase(stationsKeywordsList)
                        SharedPreferencesHelper.invoke(getApplication()).saveTimeOfUpd(System.nanoTime())
                    }

                    // get an error
                    override fun onError(e: Throwable) {
                        mErrorMsg.value = e.localizedMessage
                        mIsLoading.value = false
                    }
                })
        )
    }

    // initiate and handle data in stations database
    private fun storeStationsInDatabase(stationsList: List<Station>) {
        //launch code in separate thread in Coroutine scope
        launch {
            val dao = StationsDatabase(getApplication()).stationDAO()
            dao.deleteAllStations()
            // argument is an expanded list of individual elements
            val result = dao.insertAll(*stationsList.toTypedArray())
            // update receiver list with assigning uuId to the right objects
            for (i in stationsList.indices) {
                stationsList[i].uuid = result[i].toInt()
            }
            Log.i("StationsInDb", "storeStationsInDatabase: ")
            retrieveStations(stationsList as ArrayList<Station>)
        }
    }

    // initiate and handle data in stations keywords database
    private fun storeStationsKeywordsInDatabase(stationsKeywordsList: List<StationKeyword>) {
        //launch code in separate thread in Coroutine scope
        launch {
            val dao = StationsKeywordsDatabase(getApplication()).stationsKeywordsDAO()
            dao.deleteAllStationsKeywords()
            // argument is an expanded list of individual elements
            val result = dao.insertAll(*stationsKeywordsList.toTypedArray())
            // update receiver list with assigning uuId to the right objects
            for (i in stationsKeywordsList.indices) {
                stationsKeywordsList[i].uuid = result[i].toInt()
            }
            Log.i("StationsInDb", "storeStationsKeywordsInDatabase: ")
            retrieveStationsKeywords(stationsKeywordsList as ArrayList<StationKeyword>)
        }
    }

    // TODO when last less when 24 hours after the last update form API
    // get data from stations database
    private fun fetchStationsFromDatabase(){
        launch {
            val stationsFromDb = StationsDatabase.invoke(getApplication()).stationDAO().getAllStations()
            retrieveStations(stationsFromDb as ArrayList<Station>)
        }
    }

    // TODO when last less when 24 hours after the last update form API
    // get data from stations kezwords database
    private fun fetchStationsKeywordsFromDatabase(){
        launch {
            val stationsKeywordsFromDb = StationsKeywordsDatabase.invoke(getApplication()).stationsKeywordsDAO().getAllStationsKeywords()
            retrieveStationsKeywords(stationsKeywordsFromDb as ArrayList<StationKeyword>)
        }
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