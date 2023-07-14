package com.cammace.comicscroll

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Initialize and Configure app when app process is created.
 */
@HiltAndroidApp
class ComicScrollApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun onCreate() {
        super.onCreate()

        // initialize the Timber logging library.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
