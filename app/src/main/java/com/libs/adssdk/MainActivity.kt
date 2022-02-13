package com.libs.adssdk

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.libs.adssdk.databinding.ActivityMainBinding
import com.libs.commonadssdk.AdmobAd
import com.libs.commonadssdk.FacebookAd

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var admobAds: AdmobAd
    private lateinit var facebookAd: FacebookAd

    private var FB_INTERSTITIAL_PLACEMENT_ID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        admobAds = AdmobAd(this, BuildConfig.DEBUG, null)
        admobAds.inflateBannerAd("", binding.adViewLayout)

        binding.btnShowInterstitial.setOnClickListener {
            admobAds.showInterstitialAd("") {
                Toast.makeText(this, "Ad Dismissed", Toast.LENGTH_SHORT).show()
            }
        }

        facebookAds()
    }

    private fun facebookAds() {
        facebookAd = FacebookAd(this, getInstallerPackageName(this))
        binding.btnFbInterstitial.setOnClickListener {
            facebookAd.setupAndShowInterstitialAd(FB_INTERSTITIAL_PLACEMENT_ID, true) {
                Toast.makeText(this, "Ad Dismissed", Toast.LENGTH_SHORT).show()
            }
        }

        facebookAd.inflateMedRectAd("", binding.adViewLayout)
    }

    override fun onResume() {
        super.onResume()
        if (::admobAds.isInitialized) {
            admobAds.loadInterstitialAd("")
        }
        if (::facebookAd.isInitialized) {
            facebookAd.setupAndShowInterstitialAd(FB_INTERSTITIAL_PLACEMENT_ID, false, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::facebookAd.isInitialized) {
            facebookAd.destroyAds()
        }
    }

    fun getInstallerPackageName(context: Context): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.packageManager.getInstallSourceInfo(BuildConfig.APPLICATION_ID).initiatingPackageName
        }
        return context.packageManager.getInstallerPackageName(BuildConfig.APPLICATION_ID)
    }
}