package com.libs.adssdk

import android.content.Context
import android.os.Build

object AppUtility {
    fun getInstallerPackageName(context: Context): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.packageManager.getInstallSourceInfo(BuildConfig.APPLICATION_ID).initiatingPackageName
        }
        return context.packageManager.getInstallerPackageName(BuildConfig.APPLICATION_ID)
    }
}