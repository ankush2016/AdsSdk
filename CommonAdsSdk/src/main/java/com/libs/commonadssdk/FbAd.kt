package com.libs.commonadssdk

import android.content.Context
import android.util.Log
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.InterstitialAd


class FbAd(private val context: Context, private val isDebug: Boolean) : AudienceNetworkAds.InitListener {

    private var mInterstitialAd: InterstitialAd? = null

    init {
        if (!AudienceNetworkAds.isInitialized(context)) {
            if (isDebug) {
                AdSettings.turnOnSDKDebugger(context)
            }
            AudienceNetworkAds.buildInitSettings(context)
                .withInitListener(this)
                .initialize()
        }
    }

    override fun onInitialized(result: AudienceNetworkAds.InitResult?) {
        if (isDebug) {
            Log.e("ANKUSH", "${result?.message}")
        }
    }

    fun loadInterstitialAd(interstitialPlacementId: String) {
        val placementId = if (isDebug) "YOUR_PLACEMENT_ID" else interstitialPlacementId
        mInterstitialAd = InterstitialAd(context, placementId)
        mInterstitialAd?.loadAd()
    }

    fun showInterstitialAd(interstitialPlacementId: String, callback: () -> Unit) {
        mInterstitialAd?.isAdLoaded?.let { isLoaded ->
            if (isLoaded) {
                mInterstitialAd?.show()
            } else {
                loadInterstitialAd(interstitialPlacementId)
            }
        } ?: run {
            callback()
        }

        mInterstitialAd
    }

}