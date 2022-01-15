package com.libs.commonadssdk

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.LinearLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.lang.Exception

class AdmobAdsV2 private constructor(builder: Builder) {

    private val context = builder.context
    private val isDebug = builder.isDebug
    private var bannerAdId: String? = null
    private var interstitialAdId: String? = null
    private var adViewLayout: LinearLayout? = null

    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null

    init {
        MobileAds.initialize(context) {}
        this.bannerAdId = builder.bannerAdId
        this.interstitialAdId = builder.interstitialAdId
        this.adViewLayout = builder.adViewLayout
    }

    fun inflateBannerAd() {
        if (TextUtils.isEmpty(bannerAdId)) {
            throw Exception("Banner Ad id can't be empty")
        }
        if (adViewLayout == null) {
            throw NullPointerException("adViewLayout can't be null")
        }
        val adView = AdView(context).apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = if (isDebug) "ca-app-pub-3940256099942544/6300978111" else bannerAdId
        }
        adView.loadAd(adRequest)
        adViewLayout?.addView(adView)
    }

    fun loadInterstitialAd() {
        if (TextUtils.isEmpty(interstitialAdId)) {
            throw Exception("Interstitial Ad id can't be empty")
        }
        val adUnitId = if (isDebug) "ca-app-pub-3940256099942544/1033173712" else interstitialAdId
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun showInterstitialAd(callback: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
        } else {
            loadInterstitialAd()
            callback()
        }

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                callback()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
    }

    data class Builder(
        val context: Context, val isDebug: Boolean
    ) {
        var bannerAdId: String? = null
            private set
        var interstitialAdId: String? = null
            private set
        var adViewLayout: LinearLayout? = null
            private set

        fun bannerAdId(bannerAdId: String) = apply { this.bannerAdId = bannerAdId }
        fun interstitialAdId(interstitialAdId: String) = apply { this.interstitialAdId = interstitialAdId }
        fun adViewLayout(adViewLayout: LinearLayout) = apply { this.adViewLayout = adViewLayout }

        fun build() = AdmobAdsV2(this)
    }
}