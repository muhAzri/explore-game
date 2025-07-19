package com.zrifapps.exploregame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExploreGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // LeakCanary is automatically initialized in debug builds
        // No additional configuration needed as it's added as debugImplementation
    }
}
