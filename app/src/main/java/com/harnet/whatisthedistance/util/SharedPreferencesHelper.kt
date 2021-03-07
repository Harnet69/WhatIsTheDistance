package com.harnet.whatisthedistance.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import io.reactivex.disposables.CompositeDisposable

class SharedPreferencesHelper {
    companion object {
        private const val UPD_TIME = "Update time"
        private const val IS_ABOUT_SHOWED = "Is about showed"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(
            LOCK
        ) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)

            return SharedPreferencesHelper()
        }
    }


    // save current time to SharedPreferences
    fun saveTimeOfUpd(time: Long) {
        prefs?.edit(commit = true) {
            putLong(UPD_TIME, time)
        }
    }

    //getLastUpdateTime
    fun getLastUpdateTime(): Long? {
        return prefs?.getLong(UPD_TIME, 0)
    }

    // get is About app was showed
    fun getIsAboutShowed() = prefs?.getBoolean(IS_ABOUT_SHOWED, false)

    // set is About app was showed
    fun setIsAboutShowed(isShowed: Boolean) {
        prefs?.edit(commit = true) {
            putBoolean(IS_ABOUT_SHOWED, isShowed)
        }
    }
}