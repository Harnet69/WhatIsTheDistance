package com.harnet.whatisthedistance.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface StationsKeywordsAPI {
    // whole URL is https://koleo.pl/api/v2/main/station_keywords
    // header X-KOLEO-Version: 1

    // annotation used for knowing how this method can be used
    @Headers(
        "X-KOLEO-Version: 1"
    )
    @GET("api/v2/main/station_keywords")// !!!this is an end point, not all url
    fun getStationsKeywords() : Single<List<StationKeyword>>
}