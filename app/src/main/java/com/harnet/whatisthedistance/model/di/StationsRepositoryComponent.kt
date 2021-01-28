package com.harnet.whatisthedistance.model.di

import com.harnet.whatisthedistance.repository.StationsRepository
import com.harnet.whatisthedistance.viewModel.StationsListViewModel
import dagger.Component

@Component
interface StationsRepositoryComponent {
    fun getStationsRepository(): StationsRepository

    fun inject(stationsListViewModel: StationsListViewModel)
}