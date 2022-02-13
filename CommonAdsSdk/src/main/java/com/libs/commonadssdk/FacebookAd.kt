package com.libs.commonadssdk

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
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

    fun inflateMedRectAd(placementId: String, adContainer: LinearLayout) {
        var localPlacementId = placementId
        Log.e(TAG, "BUILD_TYPE -> ${BuildConfig.BUILD_TYPE}")
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            localPlacementId = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
        } else {
            if (!isAppDownloadFromPlayStore()) {
                showLog("APP NOT DOWNLOADED FROM PLAY STORE - returning")
                return
            }
        }
        val adView = AdView(context, localPlacementId, AdSize.RECTANGLE_HEIGHT_250)
        adContainer.addView(adView)
        adView.loadAd()
    }

    fun setupAndShowInterstitialAd(placementId: String, showInterstitial: Boolean, onAdDismissed: (() -> Unit)?) {
        var localPlacementId = placementId
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            localPlacementId = "VID_HD_16_9_15S_APP_INSTALL#YOUR_PLACEMENT_ID"
        } else {
            if (!isAppDownloadFromPlayStore()) {
                showLog("APP NOT DOWNLOADED FROM PLAY STORE - returning")
                onAdDismissed?.invoke()
                return
            }
        }
        if (!::interstitialAd.isInitialized) {
            interstitialAd = InterstitialAd(context, localPlacementId)
        }

        val interstitialAdListener = object : InterstitialAdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                showLog("Interstitial ad failed to load: ${adError?.errorMessage}")
            }

            override fun onAdLoaded(ad: Ad?) {
                showLog("Interstitial ad is loaded and ready to be displayed!")
            }

            override fun onAdClicked(ad: Ad?) {
                showLog("Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad?) {
                showLog("Interstitial ad impression logged!")
            }

            override fun onInterstitialDisplayed(ad: Ad?) {
                showLog("Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                showLog("Interstitial ad dismissed.")
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

    private fun showLog(message: String) {
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            Log.e(TAG, message)
        }
    }

    /*override fun onInitialized(result: AudienceNetworkAds.InitResult?) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onInitialized called -> ${result?.message}")
        }
    }*/
}