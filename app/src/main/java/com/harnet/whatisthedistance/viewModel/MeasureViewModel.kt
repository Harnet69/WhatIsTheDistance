package com.harnet.whatisthedistance.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.harnet.whatisthedistance.model.room.StationsDatabase
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.StationKeyword
import com.harnet.whatisthedistance.model.di.DaggerStationsApiServiceComponent
import com.harnet.whatisthedistance.model.retrofit.StationsApiService
import com.harnet.whatisthedistance.util.SharedPreferencesHelper
import com.harnet.whatisthedistance.util.isOnline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class MeasureViewModel(application: Application) : BaseViewModel(application) {
    // time of refreshing from API
    private val UPDATE_TIME: Long = 12_000L

    val mStations = MutableLiveData<ArrayList<Station>>()
    val mStationsKeywords = MutableLiveData<ArrayList<StationKeyword>>()
    val mIsLoading = MutableLiveData<Boolean>()
    val mErrorMsg = MutableLiveData<String>()
    val mIsInternet = MutableLiveData<Boolean>()

    fun refresh() {
        // is time to update time check and check Internet connection
        val isInternet = isOnline(getApplication())
        mIsInternet.value = isInternet

        if (isTimeForUpd() && isInternet) {
            refreshFromApi()
        } else {
            refreshFromDb()
        }
    }

    // when the time to update from API
    private fun refreshFromApi() {
        mIsInternet.value = true
        getDataFromAPIs()
    }

    // when lost less when 24 hours
    private fun refreshFromDb() {
        getDataFromDbs()
    }

    private fun getDataFromAPIs() {
        getStationsFromApi()
        getStationsKeywordsFromApi()
        launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), "Data from API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromDbs() {
        fetchStationsFromDatabase()
        fetchStationsKeywordsFromDatabase()
        launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), "Data from database", Toast.LENGTH_SHORT).show()
        }
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

        super.stationsApiService.disposable.add(
            super.stationsApiService.getStations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Station>>() {
                    override fun onSuccess(stationsList: List<Station>) {
                        //storing in a database
                        storeStationsInDatabase(stationsList)
                        SharedPreferencesHelper.invoke(getApplication())
                            .saveTimeOfUpd(System.currentTimeMillis())
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

        super.stationsApiService.disposable.add(
            super.stationsApiService.getStationsKeywords()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<StationKeyword>>() {
                    override fun onSuccess(stationsKeywordsList: List<StationKeyword>) {
                        storeStationsKeywordsInDatabase(stationsKeywordsList)
                        SharedPreferencesHelper.invoke(getApplication())
                            .saveTimeOfUpd(System.currentTimeMillis())
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

            retrieveStations(stationsList as ArrayList<Station>)
        }
    }

    // initiate and handle data in stations keywords database
    private fun storeStationsKeywordsInDatabase(stationsKeywordsList: List<StationKeyword>) {
        //launch code in separate thread in Coroutine scope
        launch {
            val dao = StationsDatabase(getApplication()).stationsKeywordsDAO()
            dao.deleteAllStationsKeywords()
            // argument is an expanded list of individual elements
            val result = dao.insertAll(*stationsKeywordsList.toTypedArray())
            // update receiver list with assigning uuId to the right objects
            for (i in stationsKeywordsList.indices) {
                stationsKeywordsList[i].uuid = result[i].toInt()
            }

            retrieveStationsKeywords(stationsKeywordsList as ArrayList<StationKeyword>)
        }
    }

    // get data from stations database
    private fun fetchStationsFromDatabase() {
        launch {
            val stationsFromDb =
                StationsDatabase.invoke(getApplication()).stationDAO().getAllStations()
            retrieveStations(stationsFromDb as ArrayList<Station>)
        }
    }

    // get data from stations kezwords database
    private fun fetchStationsKeywordsFromDatabase() {
        launch {
            val stationsKeywordsFromDb =
                StationsDatabase.invoke(getApplication()).stationsKeywordsDAO()
                    .getAllStationsKeywords()
            retrieveStationsKeywords(stationsKeywordsFromDb as ArrayList<StationKeyword>)
        }
    }

    // check if it's the time for update from API
    private fun isTimeForUpd(): Boolean {
        val lastUpd = SharedPreferencesHelper.invoke(getApplication()).getLastUpdateTime()
        if (lastUpd != 0L) {
            if (lastUpd != null) {
                return (lastUpd + UPDATE_TIME) < System.currentTimeMillis()
            }
        } else {
            mIsInternet.value = false
        }
        return true
    }

    fun isUserStationInStationsKeywords(userStationsName: String): Boolean? {
        return mStationsKeywords.value?.any { station -> station.keyword == userStationsName }
    }

    private fun getStationIdByKeyword(stationKeyword: String): Int? {
        val stations =
            mStationsKeywords.value?.filter { station -> station.keyword == stationKeyword }
        return stations?.get(0)?.stationId
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

        // if station location is zero
        if(depCoords?.let { isLocationIsZero(it) } == true || arrCoords?.let { isLocationIsZero(it) } == true){
            return null
        }

        if (depCoords != null && arrCoords != null) {
            return SphericalUtil.computeDistanceBetween(depCoords, arrCoords) / 1000
        }
        return null
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    private fun isLocationIsZero(stationsCoords: LatLng): Boolean {
        if(stationsCoords.latitude == 0.00 || stationsCoords.longitude == 0.00){
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        super.stationsApiService.disposable.clear()
    }
}