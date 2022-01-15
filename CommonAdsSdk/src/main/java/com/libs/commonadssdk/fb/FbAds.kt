package com.libs.commonadssdk.fb

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.facebook.ads.*
import java.lang.Exception

class FbAds private constructor(builder: Builder) {

    private val context = builder.context
    private val isDebug = builder.isDebug
    private val installer = builder.installer
    private var interstitialPlacementId: String? = null
    private var adDismissListener: AdDismissListener? = null

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adListener: InterstitialAdListener

    init {
        initializeFbAds()
        interstitialPlacementId = builder.interstitialPlacementId
        adDismissListener = builder.adDismissListener
    }

    private fun initializeFbAds() {
        if (!AudienceNetworkAds.isInitialized(context)) {
            if (isDebug) {
                AdSettings.turnOnSDKDebugger(context)
            }
            AudienceNetworkAds.buildInitSettings(context)
                .withInitListener {
                    if (isDebug) {
                        Log.e("ANKUSH", "${it?.message}")
                    }
                }
                .initialize()
        }
    }

    fun loadInterstitialAds() {
        if (!isDebug && !isAppDownloadFromPlayStore()) {
            return
        }
        if (TextUtils.isEmpty(interstitialPlacementId)) {
            throw Exception("Interstitial Ad id can't be empty")
        }
        var formattedPlacementId = interstitialPlacementId
        if (isDebug) {
            formattedPlacementId = "YOUR_PLACEMENT_ID"
            Log.e("ANKUSH", "formattedPlacementId = $formattedPlacementId")
        }

        interstitialAd = InterstitialAd(context, formattedPlacementId)
        adListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(p0: Ad?) {

            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                adDismissListener?.onAdDismiss()
                ad?.loadAd()
            }

            override fun onError(p0: Ad?, p1: AdError?) {
                if (isDebug) {
                    Log.e("ANKUSH", "INTERSTITIAL AD ERROR - ${p1?.errorMessage}, Error Code = ${p1?.errorCode}")
                }
            }

            override fun onAdLoaded(p0: Ad?) {
                if (isDebug) {
                    Log.e("ANKUSH", "Interstitial Ad Loaded")
                }
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        }
        val loadConfig = interstitialAd.buildLoadAdConfig().withAdListener(adListener).build()
        interstitialAd.loadAd(loadConfig)
    }

    fun showInterstitialAd() {
        if (isInterstitialAdLoaded()) {
            interstitialAd.show()
        } else {
            adDismissListener?.onAdDismiss()
        }
    }

    private fun isInterstitialAdLoaded(): Boolean {
        if (::interstitialAd.isInitialized && interstitialAd.isAdLoaded && !interstitialAd.isAdInvalidated) {
            return true
        }
        return false
    }

    data class Builder(
        val context: Context, val isDebug: Boolean, val installer: String?
    ) {
        var interstitialPlacementId: String? = null
            private set
        var adDismissListener: AdDismissListener? = null
            private set

        fun interstitialPlacementId(interstitialPlacementId: String) = apply { this.interstitialPlacementId = interstitialPlacementId }
        fun adDismissListener(adDismissListener: AdDismissListener) = apply { this.adDismissListener = adDismissListener }

        fun build() = FbAds(this)
    }

    private fun isAppDownloadFromPlayStore(): Boolean {
        val validInstallers: List<String> = ArrayList(listOf("com.android.vending", "com.google.android.feedback"))
        return !TextUtils.isEmpty(installer) && validInstallers.contains(installer)
    }

    interface AdDismissListener {
        fun onAdDismiss()
    }
}