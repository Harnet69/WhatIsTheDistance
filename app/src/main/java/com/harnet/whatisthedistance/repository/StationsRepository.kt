package com.harnet.whatisthedistance.repository

import android.content.Context
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.room.StationsDAO
import com.harnet.whatisthedistance.model.room.StationsDatabase
import javax.inject.Inject

class StationsRepository @Inject constructor() {
    lateinit var stationsDao: StationsDAO

    suspend fun getAllStations(context: Context): List<Station> {
        stationsDao = StationsDatabase.invoke(context).stationDAO()
        return stationsDao.getAllStations()
    }
}