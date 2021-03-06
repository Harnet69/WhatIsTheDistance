package com.harnet.whatisthedistance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class StationKeyword(
    @ColumnInfo(name = "station_keyword_id")
    val id: Int,
    @ColumnInfo(name = "station_keyword_keyword")
    val keyword: String,
    @ColumnInfo(name = "station_keyword_station_id")
    @SerializedName("station_id")
    val stationId: Int
){
    @PrimaryKey(autoGenerate = true) //autogenerated id for each instance
    var uuid: Int = 0
}