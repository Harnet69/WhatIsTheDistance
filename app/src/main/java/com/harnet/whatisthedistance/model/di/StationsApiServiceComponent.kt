package com.harnet.whatisthedistance.model.di

import com.harnet.whatisthedistance.model.retrofit.StationsApiService
import com.harnet.whatisthedistance.viewModel.BaseViewModel
import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import dagger.Component

@Component
interface StationsApiServiceComponent {
    fun getStationsApiService(): StationsApiService

    fun inject(baseViewModel: BaseViewModel)
}