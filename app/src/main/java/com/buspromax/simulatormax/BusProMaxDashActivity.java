package com.buspromax.simulatormax;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.buspromax.simulatormax.R;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;


public class BusProMaxDashActivity extends AppCompatActivity implements MaxRewardedAdListener {

    Button _PlayBusPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_pro_dash);

                try {
            getSupportActionBar().hide();
        }catch (Exception e){

        }


        if(BusProMaxAdsManager.busProMaxShow_Ads) {
            try {
                BusProMaxAdsManager.BusInitialiseAds(this);
                BusProMaxAdsManager.BusShowBannerAds(findViewById(R.id.AdsBusPro));


            } catch (Exception e) {
            }
        }

        _PlayBusPro =findViewById(R.id.busPlay);
        _PlayBusPro.setOnClickListener(v -> {
            Intent i = new Intent(BusProMaxDashActivity.this, BusProMaxMainActivity.class);
            startActivity(i);
        });

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }
}