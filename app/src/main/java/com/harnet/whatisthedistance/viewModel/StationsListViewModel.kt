package com.harnet.whatisthedistance.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.StationsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class StationsListViewModel(application: Application) : BaseViewModel(application) {
    // instantiate stationsApiService
    private val stationsApiService = StationsApiService()

    // allows observe observable Single<List<DogBreed>>, handle retrieving and avoid memory leaks
    // (can be produced because of waiting for observable while app has been destroyed)
    private val disposable = CompositeDisposable()

    // provide an info of actual list of dogs what we retrieve from data source
    val mStations = MutableLiveData<List<Station>>()

    //notify about generic error with data's retrieval because it listens the ViewModel
    val mStationsLoadError = MutableLiveData<Boolean>()

    // listening is data loading
    val mStationsLoading = MutableLiveData<Boolean>()

    // retrieve dogs and set UI components. Can work separately from DB
    private fun retrieveStations(stationsList: List<Station>) {
        // set received list to observable mutable list
        mStations.postValue(stationsList)
        // switch off error message
        mStationsLoadError.postValue(false)
        // switch off waiting spinner
        mStationsLoading.postValue(false)
    }

    //refresh information from remote API
    fun refreshFromAPI() {
        fetchFromRemoteWithRetrofit()
        Toast.makeText(getApplication(), "Try to get data from API", Toast.LENGTH_SHORT).show()
        // notification
//        NotificationsHelper(getApplication()).createNotification()
    }

    //refresh information from a database
    fun refreshFromDatabase() {
        //TODO
//        fetchFromDatabase()
//        Toast.makeText(getApplication(), "Database data", Toast.LENGTH_SHORT).show()
    }

    // fetches data with Retrofit library from remote API
    private fun fetchFromRemoteWithRetrofit() {
        //set loading flag to true
        mStationsLoading.value = true

        disposable.add(
            // set it to a different thread(passing this call to the background thread)
            stationsApiService.getStations()
                .subscribeOn(Schedulers.newThread())
                // retrieve it from a background to the main thread for displaying
                .observeOn(AndroidSchedulers.mainThread())
                // pass our Single object here, it's created and implemented
                .subscribeWith(object : DisposableSingleObserver<List<Station>>() {
                    // get list of stations objects
                    override fun onSuccess(stationsList: List<Station>) {
                        //store this information and time of retrieving in a db as a cache
                        //TODO implement storing in database every 24 hours
                        retrieveStations(stationsList)
//                        storeDogInDatabase(dogsList)
//                        SharedPreferencesHelper.invoke(getApplication()).saveTimeOfUpd(System.nanoTime())
                    }

                    // get an error
                    override fun onError(e: Throwable) {
                        mStationsLoadError.value = true
                        mStationsLoading.value = false
                        // print stack of error to handling it
                        Log.i("StationsList", "StationsList error: ${e.localizedMessage}")
                        e.printStackTrace()
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}