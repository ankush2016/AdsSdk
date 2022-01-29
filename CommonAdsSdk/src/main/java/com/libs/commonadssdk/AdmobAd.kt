package com.libs.commonadssdk

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.*

class AdmobAd(private val context: Context, private val isDebug: Boolean, testDeviceIds: List<String>?) {
    init {
        MobileAds.initialize(context) {}
        if (!testDeviceIds.isNullOrEmpty()) {
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }

    }

    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null


    fun inflateBannerAd(bannerAdId: String, adViewLayout: LinearLayout) {
        val adView = AdView(context).apply {
            adSize = AdSize.LARGE_BANNER
            //adUnitId = if (isDebug) "ca-app-pub-3940256099942544/6300978111" else bannerAdId
            adUnitId = bannerAdId
        }
        adView.loadAd(adRequest)
        adViewLayout.addView(adView)
    }

    fun inflateMedRectangleAd(adId: String, adViewLayout: LinearLayout) {
        val adView = AdView(context).apply {
            adSize = AdSize.MEDIUM_RECTANGLE
            //adUnitId = if (isDebug) "ca-app-pub-3940256099942544/6300978111" else bannerAdId
            adUnitId = adId
        }
        adView.loadAd(adRequest)
        adViewLayout.addView(adView)
    }

    fun loadInterstitialAd(interstitialAdId: String) {
        //val adUnitId = if (isDebug) "ca-app-pub-3940256099942544/1033173712" else interstitialAdId
        val adUnitId = interstitialAdId
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun showInterstitialAd(interstitialAdId: String, callback: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
        } else {
            loadInterstitialAd(interstitialAdId)
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