package com.harnet.whatisthedistance.repository

import com.harnet.whatisthedistance.model.Station

interface StationRepositoryInterface {
    suspend fun getAllStations(): List<Station>
}