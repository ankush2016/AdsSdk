package com.libs.adssdk

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ActivityCallbacks.registerLifecycleCallbacks(this)
    }
}