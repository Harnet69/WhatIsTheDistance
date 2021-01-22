package com.harnet.whatisthedistance.model.retrofit

import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.StationKeyword
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class StationsApiService @Inject constructor() {
    val disposable = CompositeDisposable()
    // base URL of the API
    private val BASE_URL = "https://koleo.pl"

    // object created by Retrofit for accessing to an endpoint
    private val stationsApi =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        // handle all basic communication, separate threads, errors and converts JSON to object of our class
        .addConverterFactory(GsonConverterFactory.create())
        // convert this object to observable Single<List<>>
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(StationsAPI::class.java)// create model class

    private val stationsKeywordsApi =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        // convert this object to observable Single<List<>>
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(StationsKeywordsAPI::class.java)// create model class

    //get observable List of stations from API
    fun getStations(): Single<List<Station>> {
        return stationsApi.getStations()
    }

    //get observable List of stations keywords from API
    fun getStationsKeywords(): Single<List<StationKeyword>> {
        return stationsKeywordsApi.getStationsKeywords()
    }
}