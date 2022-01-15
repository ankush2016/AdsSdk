package com.libs.adssdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver

object ActivityCallbacks : Application.ActivityLifecycleCallbacks, LifecycleObserver {
    fun registerLifecycleCallbacks(context: Context) {
        val appContext = context.applicationContext
        if (appContext is Application) {
            appContext.registerActivityLifecycleCallbacks(this)
        }
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
//        Log.e("ANKUSH", "onActivityCreated ${activity.localClassName}")
    }

    override fun onActivityStarted(p0: Activity) {
//        Log.e("ANKUSH", "onActivityStarted")
    }

    override fun onActivityResumed(p0: Activity) {
//        Log.e("ANKUSH", "onActivityResumed")
    }

    override fun onActivityPaused(p0: Activity) {
//        Log.e("ANKUSH", "onActivityPaused")
    }

    override fun onActivityStopped(p0: Activity) {
//        Log.e("ANKUSH", "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
//        Log.e("ANKUSH", "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(p0: Activity) {
//        Log.e("ANKUSH", "onActivityDestroyed")
    }
}