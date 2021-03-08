package com.harnet.whatisthedistance.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.harnet.whatisthedistance.adapter.StationsListAdapter
import javax.inject.Inject

class FragmentFactory @Inject constructor(
    private val stationsListAdapter: StationsListAdapter,
    private val glide: RequestManager
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        // check what class it is and add an appropriate dependency
        return when (className) {
            StationsListFragment::class.java.name -> StationsListFragment(stationsListAdapter)
//            ArtsFragment::class.java.name -> ArtsFragment(artRecyclerAdapter)
//            SearchFragment::class.java.name -> SearchFragment(searchRecyclerAdapter)
//            ArtAddingFragment::class.java.name -> ArtAddingFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}