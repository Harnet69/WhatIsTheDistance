package com.harnet.whatisthedistance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Station(
    @ColumnInfo(name = "station_id")
    val id: Int,
    @ColumnInfo(name = "station_name")
    val name: String,
    @ColumnInfo(name = "name_slug")
    @SerializedName("name_slug")
    val nameSlug: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
    @ColumnInfo(name = "longitude")
    val longitude: Double?,
    @ColumnInfo(name = "hits")
    val hits: String,
    @ColumnInfo(name = "ibnr")
    val ibnr: String?,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "region")
    val region: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "localised_name")
    @SerializedName("localised_name")
    val localisedName: String?
){
    @PrimaryKey(autoGenerate = true) //autogenerated id for each instance
    var uuid: Int = 0
}