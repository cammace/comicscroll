package com.cammace.comicscroll

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Initialize and Configure app when app process is created.
 */
@HiltAndroidApp
class ComicScrollApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize the Timber logging library.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
