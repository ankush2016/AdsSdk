package com.libs.adssdk

import android.app.Notification
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.libs.adssdk.databinding.ActivityMainBinding
import com.libs.commonadssdk.AdmobAd
import com.libs.commonadssdk.AdmobAds
import com.libs.commonadssdk.AdmobAdsV2
import com.libs.commonadssdk.fb.FbAds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var admobAds: AdmobAd
    private lateinit var admobAdsBuilderPatternOld: AdmobAds
    private lateinit var admobAdsV2: AdmobAdsV2

    private lateinit var fbAds: FbAds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdmobV1()
        setupBuilderAdmob()
        setupBuilderAdmobV2()

        setupFbAds()
    }

    private fun setupFbAds() {
        fbAds = FbAds.Builder(this, BuildConfig.DEBUG, AppUtility.getInstallerPackageName(this))
            .interstitialPlacementId("3043368849237036_3043369389236982")
            .adDismissListener(object : FbAds.AdDismissListener {
                override fun onAdDismiss() {

                }
            })
            .build()

        binding.btnShowFbAd.setOnClickListener {
            fbAds.showInterstitialAd()
        }
    }

    private fun setupBuilderAdmobV2() {
        admobAdsV2 = AdmobAdsV2.Builder(this, BuildConfig.DEBUG)
            .adViewLayout(binding.adViewLayout)
            .bannerAdId("ca-app-pub-3940256099942544/6300978111")
            .interstitialAdId("ca-app-pub-3940256099942544/1033173712")
            .build()

        admobAdsV2.inflateBannerAd()

        binding.btnShowInterstitialByBuilder.setOnClickListener {
            admobAdsV2.showInterstitialAd {
                Toast.makeText(this, "Ad Dismissed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAdmobV1() {
        admobAds = AdmobAd(this, BuildConfig.DEBUG)
        admobAds.inflateBannerAd("", binding.adViewLayout)

        binding.btnShowInterstitial.setOnClickListener {
            admobAds.showInterstitialAd("") {
                Toast.makeText(this, "Ad Dismissed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBuilderAdmob() {
        admobAdsBuilderPatternOld = AdmobAds.Builder(this, BuildConfig.DEBUG).apply {
            setAdViewLayout(binding.adViewLayout)
            setBannerAdId("ca-app-pub-3940256099942544/6300978111")
            setInterstitialAdId("ca-app-pub-3940256099942544/1033173712")
        }.build()

        admobAdsBuilderPatternOld.inflateBannerAd()

        binding.btnShowInterstitialV2.setOnClickListener {
            admobAdsBuilderPatternOld.showInterstitialAd {
                Toast.makeText(this, "Ad Dismissed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::admobAds.isInitialized) {
            admobAds.loadInterstitialAd("")
        }

        if (::admobAdsBuilderPatternOld.isInitialized) {
            admobAdsBuilderPatternOld.loadInterstitialAd()
        }

        if (::admobAdsV2.isInitialized) {
            admobAdsV2.loadInterstitialAd()
        }

        if (::fbAds.isInitialized) {
            fbAds.loadInterstitialAds()
        }
    }
}