package com.harnet.whatisthedistance.repository

import com.harnet.whatisthedistance.model.Station

class StationRepositoryFake: StationRepositoryInterface {
    override suspend fun getAllStations(): List<Station> {
        return arrayListOf(Station(1, "Station1", "Station1 slug", 0.0, 0.0, "1", "0",
        "City1", "Reg1", "Country1", "loc_name1"),Station(2, "Station2", "Station2 slug", 0.0, 0.0, "2", "0",
        "City2", "Reg2", "Country2", "loc_name2"),Station(3, "Station3", "Station3 slug", 0.0, 0.0, "3", "0",
        "City3", "Reg3", "Country3", "loc_name3"))
    }
}