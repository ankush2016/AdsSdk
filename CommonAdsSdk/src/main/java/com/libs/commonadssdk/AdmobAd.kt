package com.libs.commonadssdk

import android.content.Context
import android.widget.LinearLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class AdmobAd(private val context: Context) {
    init {
        MobileAds.initialize(context) {}
    }

    fun inflateBannerAd(bannerAdId: String, adViewLayout: LinearLayout) {
        val adView = AdView(context).apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = bannerAdId
        }
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adViewLayout.addView(adView)
    }
}