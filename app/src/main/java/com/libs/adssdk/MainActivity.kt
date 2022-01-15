package com.libs.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.libs.adssdk.databinding.ActivityMainBinding
import com.libs.commonadssdk.AdmobAd

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var admobAds: AdmobAd

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
    }

    override fun onResume() {
        super.onResume()
        if (::admobAds.isInitialized) {
            admobAds.loadInterstitialAd("")
        }
    }
}