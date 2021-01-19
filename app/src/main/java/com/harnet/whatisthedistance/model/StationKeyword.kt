package com.harnet.whatisthedistance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StationKeyword(
    @ColumnInfo(name = "station_keyword_id")
    val id: Int,
    @ColumnInfo(name = "station_keyword_keyword")
    val keyword: String,
    @ColumnInfo(name = "station_keyword_station_id")
    val station_id: Int
){
    @PrimaryKey(autoGenerate = true) //autogenerated id for each DogBreed instance
    var uuid: Int = 0
}