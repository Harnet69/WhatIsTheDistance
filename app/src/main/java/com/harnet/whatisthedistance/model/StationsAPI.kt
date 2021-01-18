package com.harnet.whatisthedistance.model

import io.reactivex.Single
import retrofit2.http.GET

interface StationsAPI {
    // whole URL is https://koleo.pl/api/v2/main/station_keywords
    // header X-KOLEO-Version: 1
    //TODO here can be a problem with API, because of unused header

    // annotation used for knowing how this method can be used
    @GET("api/v2/main/stations")// !!!this is an end point, not all url
    fun getStations(): Single<List<Station>>
}