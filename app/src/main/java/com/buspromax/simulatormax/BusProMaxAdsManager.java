package com.buspromax.simulatormax;

import android.app.Activity;
import android.view.View;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;

public class BusProMaxAdsManager {
    public static MaxRewardedAd busProMaxRewardedAd;
    public static MaxInterstitialAd busProMaxInterstitialAd;
    public static boolean busProMaxShow_Ads = true;

    public static void BusInitialiseAds(Activity activity) {
        if (BusProMaxAdsManager.busProMaxShow_Ads) {
            AppLovinSdk.getInstance(activity).setMediationProvider("max");
            AppLovinSdk.initializeSdk(activity, configuration -> {
                try {
                    busProMaxInterstitialAd = new MaxInterstitialAd(activity.getResources().getString(R.string.intertial_ads), activity);
                    busProMaxInterstitialAd.loadAd();
                    busProMaxInterstitialAd.setRevenueListener(new MaxAdRevenueListener() {
                        @Override
                        public void onAdRevenuePaid(MaxAd ad) {

                        }
                    });
                } catch (Exception e) {

                }
                try {
                    busProMaxRewardedAd = MaxRewardedAd.getInstance(activity.getResources().getString(R.string.rewarded_ad), activity);
                    busProMaxRewardedAd.loadAd();
                    busProMaxRewardedAd.setRevenueListener(ad -> {

                    });
                } catch (Exception e) {

                }

            });
        }
    }

    public static void BusShowBannerAds(MaxAdView maxAdView) {
        if (BusProMaxAdsManager.busProMaxShow_Ads) {
            maxAdView.setVisibility(View.VISIBLE);
            maxAdView.loadAd();
        } else {
            maxAdView.setVisibility(View.GONE);
        }
    }

}
