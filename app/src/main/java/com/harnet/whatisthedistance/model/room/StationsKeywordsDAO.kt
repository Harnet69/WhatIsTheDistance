package com.harnet.whatisthedistance.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harnet.whatisthedistance.model.StationKeyword

@Dao
interface StationsKeywordsDAO {
    @Insert
    suspend fun insertAll(vararg stationsKeywords: StationKeyword): List<Long> // stations keywords is an expanded list of individual elements!!!

    @Query("SELECT * FROM stationkeyword")
    suspend fun getAllStationsKeywords(): List<StationKeyword>

    @Query("DELETE FROM stationkeyword")
    suspend fun deleteAllStationsKeywords()

    @Query("SELECT * FROM stationkeyword WHERE uuid = :stationUuid")
    suspend fun getStationKeyword(stationUuid: Int): StationKeyword

    @Query("SELECT * FROM stationkeyword WHERE station_keyword_id = :id")
    suspend fun getStationKeyword(id: String): StationKeyword

    @Query("SELECT sk.* FROM stationkeyword as sk JOIN station AS s ON sk.station_keyword_station_id = s.station_id ORDER BY s.hits DESC")
    suspend fun getStationsKeywordsOrderedByHits(): List<StationKeyword>
}