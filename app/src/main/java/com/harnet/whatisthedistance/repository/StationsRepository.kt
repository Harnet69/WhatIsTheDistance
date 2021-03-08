package com.harnet.whatisthedistance.repository

import com.harnet.whatisthedistance.model.Station
import com.harnet.whatisthedistance.model.room.StationsDAO
import javax.inject.Inject

class StationsRepository @Inject constructor(private val stationDAO: StationsDAO): StationRepositoryInterface {

    override suspend fun getAllStations(): List<Station> {
        //TODO here can be the problem
//        return StationsDatabase.invoke(context).stationDAO().getAllStations()
        return stationDAO.getAllStations()
    }
}