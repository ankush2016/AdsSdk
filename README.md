# AdsSdk

---
# Admob Banner & Interstitial Ad Usage
---


AndroidManifest.xml - meta-data
---
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"
    

Activity
---

    private lateinit var binding: ActivityMainBinding
    private lateinit var admobAds: AdmobAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        admobAds = AdmobAd(this, BuildConfig.DEBUG)
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
