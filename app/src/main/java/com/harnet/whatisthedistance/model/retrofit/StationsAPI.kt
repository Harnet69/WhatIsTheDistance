package com.harnet.whatisthedistance.model.retrofit

import com.harnet.whatisthedistance.model.Station
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface StationsAPI {
    // whole URL is https://koleo.pl/api/v2/main/station_keywords !!! header X-KOLEO-Version: 1

    @Headers(
        "X-KOLEO-Version: 1"
    )
    @GET("api/v2/main/stations")
    fun getStations(): Single<List<Station>>
}