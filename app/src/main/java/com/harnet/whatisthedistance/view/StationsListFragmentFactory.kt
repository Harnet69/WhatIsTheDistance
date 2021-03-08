package com.harnet.whatisthedistance.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class StationsListFragmentFactory @Inject constructor(): FragmentFactory(){
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        // check what class it is and add an appropriate dependency
        return when(className){
                StationsListFragment::class.java.name -> StationsListFragment()
//            ArtsFragment::class.java.name -> ArtsFragment(artRecyclerAdapter)
//            SearchFragment::class.java.name -> SearchFragment(searchRecyclerAdapter)
//            ArtAddingFragment::class.java.name -> ArtAddingFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}