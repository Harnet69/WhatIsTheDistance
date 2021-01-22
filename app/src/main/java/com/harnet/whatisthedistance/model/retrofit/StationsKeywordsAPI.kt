package com.harnet.whatisthedistance.model.retrofit

import com.harnet.whatisthedistance.model.StationKeyword
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface StationsKeywordsAPI {
    // whole URL is https://koleo.pl/api/v2/main/station_keywords !!!header X-KOLEO-Version: 1

    @Headers(
        "X-KOLEO-Version: 1"
    )
    @GET("api/v2/main/station_keywords")
    fun getStationsKeywords(): Single<List<StationKeyword>>
}