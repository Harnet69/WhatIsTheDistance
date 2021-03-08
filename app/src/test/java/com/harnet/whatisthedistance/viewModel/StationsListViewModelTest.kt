package com.harnet.whatisthedistance.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.harnet.arttesting.MainCoroutineRule
import com.harnet.arttesting.viewModel.getOrAwaitValueTest
import com.harnet.whatisthedistance.repository.StationRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class StationsListViewModelTest {
    private lateinit var viewModel: StationsListViewModel

    //runs all in the Main thread
    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    //because in Test folder there is not an emulator and no Main thread
    @get:Rule
    var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        viewModel = StationsListViewModel(StationRepositoryFake())
    }

    @Test
    fun`refresh from API returns stations list`(){
        viewModel.refreshFromAPI()
        val value = viewModel.mStations.getOrAwaitValueTest()
        assertThat(value.size).isEqualTo(3)
    }

}