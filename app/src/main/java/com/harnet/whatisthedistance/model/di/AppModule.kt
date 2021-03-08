package com.harnet.whatisthedistance.model.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.harnet.whatisthedistance.R
import com.harnet.whatisthedistance.model.retrofit.StationsAPI
import com.harnet.whatisthedistance.model.retrofit.StationsKeywordsAPI
import com.harnet.whatisthedistance.model.room.StationsDAO
import com.harnet.whatisthedistance.model.room.StationsDatabase
import com.harnet.whatisthedistance.repository.StationRepositoryInterface
import com.harnet.whatisthedistance.repository.StationsRepository
import com.harnet.whatisthedistance.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
// provide LifeCircle for provider functions
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, StationsDatabase::class.java, "stationsDatabase").build()

    @Singleton
    @Provides
    fun injectStationDao(database: StationsDatabase) = database.stationDAO()

    @Singleton
    @Provides
    fun injectStationsKeywordsDAO(database: StationsDatabase) = database.stationsKeywordsDAO()

    @Singleton
    @Provides
    fun injectStationsAPI(): StationsAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(StationsAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectStationsKeywordsAPI(): StationsKeywordsAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(StationsKeywordsAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectArtRepository(stationsDAO: StationsDAO) = StationsRepository(stationsDAO) as StationRepositoryInterface


    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_distance)
                .error(R.drawable.ic_launcher_foreground)
        )
}