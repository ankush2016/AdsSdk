package com.libs.commonadssdk

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.facebook.ads.*

class FacebookAd(private val context: Context, private val installer: String?) {//: AudienceNetworkAds.InitListener {

    private val TAG = "FacebookAd"
    private val BUILD_TYPE_DEBUG = "debug"
    private lateinit var interstitialAd: InterstitialAd

    init {
        AudienceNetworkAds.initialize(context)
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            AdSettings.turnOnSDKDebugger(context)
        }
        AudienceNetworkAds
            .buildInitSettings(context)
            //.withInitListener(this)
            .initialize()
    }

    fun setupAndShowInterstitialAd(placementId: String, showInterstitial: Boolean, onAdDismissed: (() -> Unit)?) {
        var localPlacementId = placementId
        //if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            localPlacementId = "VID_HD_16_9_15S_APP_INSTALL#YOUR_PLACEMENT_ID"
        //} else {
            if (isAppDownloadFromPlayStore()) {
                return
            }
        //}
        if (!::interstitialAd.isInitialized) {
            interstitialAd = InterstitialAd(context, localPlacementId)
        }

        val interstitialAdListener = object : InterstitialAdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                //if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad failed to load: ${adError?.errorMessage}")
                //}
            }

            override fun onAdLoaded(ad: Ad?) {
                //if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad is loaded and ready to be displayed!")
                //}
            }

            override fun onAdClicked(ad: Ad?) {
                //if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad clicked!");
                //}
            }

            override fun onLoggingImpression(ad: Ad?) {
//                if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad impression logged!")
//                }
            }

            override fun onInterstitialDisplayed(ad: Ad?) {
//                if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad displayed.")
//                }
            }

            override fun onInterstitialDismissed(ad: Ad?) {
//                if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
                    Log.e(TAG, "Interstitial ad dismissed.")
//                }
                onAdDismissed?.invoke()
                interstitialAd.loadAd()
            }
        }

        interstitialAd.loadAd(
            interstitialAd
                .buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
        if (showInterstitial) {
            if (::interstitialAd.isInitialized && interstitialAd.isAdLoaded) {
                interstitialAd.show()
            } else {
                onAdDismissed?.invoke()
            }
        }
    }

    fun destroyAds() {
        if (::interstitialAd.isInitialized) {
            interstitialAd.destroy()
        }
    }

    private fun isAppDownloadFromPlayStore(): Boolean {
        val validInstallers: List<String> = ArrayList(listOf("com.android.vending", "com.google.android.feedback"))
        return !TextUtils.isEmpty(installer) && validInstallers.contains(installer)
    }

    /*override fun onInitialized(result: AudienceNetworkAds.InitResult?) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onInitialized called -> ${result?.message}")
        }
    }*/
}