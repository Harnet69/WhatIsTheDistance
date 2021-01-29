package com.harnet.whatisthedistance.repository

import android.content.Context
import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.room.StationsDatabase
import javax.inject.Inject

class StationsRepository @Inject constructor() {

    suspend fun getAllStations(context: Context): List<Station> {
        return StationsDatabase.invoke(context).stationDAO().getAllStations()
    }
}