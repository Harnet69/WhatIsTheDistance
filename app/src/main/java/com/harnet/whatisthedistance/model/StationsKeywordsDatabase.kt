package com.harnet.whatisthedistance.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harnet.dogbreeds.model.StationsDAO
import com.harnet.whatisthedistance.model.Station

// singleton for handling with a database
@Database(entities = arrayOf(StationKeyword::class), version = 1)
abstract class StationsKeywordsDatabase : RoomDatabase() {
    // return StationKeywords DAO interface
    abstract fun stationsKeywordsDAO(): StationsKeywordsDAO

    // create static methods and variables which can accessed from outside the class
    companion object {
        //`volatile` meaning that writes to this field are immediately made visible to other threads
        @Volatile
        private var instance: StationsKeywordsDatabase? = null
        private val LOCK = Any()

        //return instance if instance is null
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            // create an instance of DogDatabase
            instance ?: buildDatabase(context).also {
                // attach an instance to variable and return an instance to invoker
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, // because usual context can be null(f ex. user rotates a device)
            StationsKeywordsDatabase::class.java,
            "stationsKeywordsDatabase"
        ).build()
    }
}