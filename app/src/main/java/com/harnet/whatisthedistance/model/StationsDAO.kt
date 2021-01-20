package com.harnet.dogbreeds.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harnet.whatisthedistance.model.Station

@Dao
interface StationsDAO {
    @Insert
    suspend fun insertAll(vararg stations: Station): List<Long> // dogs is an expanded list of individual elements!!!

    @Query("SELECT * FROM station")
    suspend fun getAllStations(): List<Station>

    @Query("DELETE FROM station")
    suspend fun deleteAllStations()

    @Query("SELECT * FROM station WHERE uuid = :stationUuid")
    suspend fun getStation(stationUuid: Int): Station

    @Query("SELECT * FROM station WHERE station_id = :id")
    suspend fun getStation(id: String): Station
}