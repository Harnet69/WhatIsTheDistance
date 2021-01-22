package com.harnet.whatisthedistance.model.di

import com.harnet.whatisthedistance.model.retrofit.StationsApiService
import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import dagger.Component

@Component
interface StationsApiServiceComponent {
    fun getStationsApiService(): StationsApiService

    fun inject(measureViewModel: MeasureViewModel)
}