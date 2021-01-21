package com.harnet.whatisthedistance.model

import com.harnet.whatisthedistance.viewModel.MeasureViewModel
import dagger.Component

@Component
interface StationsApiServiceComponent {
    fun getStationsApiService(): StationsApiService

    fun inject(measureViewModel: MeasureViewModel)
}