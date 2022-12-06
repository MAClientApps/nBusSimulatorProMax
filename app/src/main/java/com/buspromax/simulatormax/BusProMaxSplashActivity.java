package com.buspromax.simulatormax;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.adjust.sdk.Adjust;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BusProMaxSplashActivity extends AppCompatActivity {
    private long BusPro_REFF_TIMER = 10;
    int BusPro_timer = 0;
    ScheduledExecutorService BusPro_executorService;
    private long BusPro_APP_TIMER = 16;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bus_pro_splash);
        try {
            getSupportActionBar().hide();
        }catch (Exception e){

        }
        try {
            Adjust.getGoogleAdId(this, googleAdId -> {
                try {
                    BusProMaxUtils.saveBusPro_GPSADID(BusProMaxSplashActivity.this,
                            googleAdId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        runBusProScheduledExecutorService();
    }

    public void sendToBusProNext() {
        Intent intentNext_DirtRide = new Intent(BusProMaxSplashActivity.this,
                BusProMaxRideActivity.class);
        startActivity(intentNext_DirtRide);
        finish();
    }
    public void sendToBusProMain() {
        Intent intent = new Intent(this, BusProMaxDashActivity.class);
        startActivity(intent);
        finish();
    }
    public void runBusProScheduledExecutorService() {
        try {
            if (!BusProMaxUtils.isBusPro_SecondOpen(BusProMaxSplashActivity.this)) {
                BusProMaxUtils.saveBusPro_SecondOpen(BusProMaxSplashActivity.this, true);
                BusPro_executorService = Executors.newScheduledThreadPool(5);
                BusPro_executorService.scheduleAtFixedRate(() -> {
                    BusPro_timer = BusPro_timer + 1;
                    if (!BusProMaxUtils.getBusPro_link(BusProMaxSplashActivity.this).isEmpty()) {
                        try {
                            BusPro_executorService.shutdown();
                        } catch (Exception DirtRide_exception) {
                        }
                        sendToBusProNext();
                    } else if (BusPro_timer >= BusPro_REFF_TIMER) {
                        if (!BusProMaxUtils.getBusPro_AdjustAttribute(BusProMaxSplashActivity.this).isEmpty()) {
                            try {
                                BusPro_executorService.shutdown();
                            } catch (Exception DirtRide_exception) {
                            }
                            if (!BusProMaxUtils.getBusPro_Campaign(BusProMaxSplashActivity.this).isEmpty()
                                    && BusProMaxUtils.isValidBusPro_Campaign(BusProMaxSplashActivity.this)){
                                sendToBusProNext();
                            }else{
                                sendToBusProMain();
                            }
                        }else if (BusPro_timer >= BusPro_APP_TIMER) {
                            try {
                                BusPro_executorService.shutdown();
                            } catch (Exception DirtRide_exception) {
                            }
                            sendToBusProMain();
                        }
                    }

                }, 0, 500, TimeUnit.MILLISECONDS);
            } else {
                if (!BusProMaxUtils.getBusPro_link(BusProMaxSplashActivity.this).isEmpty()) {
                    sendToBusProNext();
                    return;
                }
                if (!BusProMaxUtils.getBusPro_AdjustAttribute(BusProMaxSplashActivity.this).isEmpty()
                        && !BusProMaxUtils.getBusPro_Campaign(BusProMaxSplashActivity.this).isEmpty()
                        && BusProMaxUtils.isValidBusPro_Campaign(BusProMaxSplashActivity.this)) {
                    sendToBusProNext();
                    return;
                }
                sendToBusProMain();
            }
        }catch (Exception DirtRide_exception){
            sendToBusProMain();
        }
    }
    
}