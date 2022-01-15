package com.libs.commonadssdk

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.LinearLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.lang.Exception

class AdmobAds private constructor(private val builder: Builder) {

    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null

    private var bannerAdId: String? = null
    private var interstitialAdId: String? = null
    private var adViewLayout: LinearLayout? = null

    class Builder(val context: Context, val isDebug: Boolean) {
        private var bannerAdId: String? = null
        private var interstitialAdId: String? = null
        private var adViewLayout: LinearLayout? = null

        fun setBannerAdId(adId: String) = apply { this.bannerAdId = adId }
        fun setInterstitialAdId(adId: String) = apply { this.interstitialAdId = adId }
        fun setAdViewLayout(layout: LinearLayout) = apply { this.adViewLayout = layout }
        fun build() = AdmobAds(this)

        fun getBannerAdId() = bannerAdId
        fun getInterstitialAdId() = interstitialAdId
        fun getAdViewLayout() = adViewLayout
    }

    init {
        MobileAds.initialize(builder.context) {}
        bannerAdId = builder.getBannerAdId()
        interstitialAdId = builder.getInterstitialAdId()
        adViewLayout = builder.getAdViewLayout()
    }

    fun inflateBannerAd() {
        if (TextUtils.isEmpty(bannerAdId)) {
            throw Exception("Banner Ad id can't be empty")
        }
        if (adViewLayout == null) {
            throw NullPointerException("adViewLayout can't be null")
        }
        val adView = AdView(builder.context).apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = if (builder.isDebug) "ca-app-pub-3940256099942544/6300978111" else bannerAdId
        }
        adView.loadAd(adRequest)
        adViewLayout?.addView(adView)
    }

    fun loadInterstitialAd() {
        if (TextUtils.isEmpty(interstitialAdId)) {
            throw Exception("Interstitial Ad id can't be empty")
        }
        val adUnitId = if (builder.isDebug) "ca-app-pub-3940256099942544/1033173712" else interstitialAdId
        InterstitialAd.load(builder.context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
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
            mInterstitialAd?.show(builder.context as Activity)
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
}