package com.harnet.whatisthedistance.repository

import android.content.Context
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.room.StationsDatabase

class StationsRepository(context: Context) {
    private val stationsDao = StationsDatabase.invoke(context).stationDAO()

    suspend fun getAllStations(): List<Station>? {
        return stationsDao.getAllStations()
    }
}