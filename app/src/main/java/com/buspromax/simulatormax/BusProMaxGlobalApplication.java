package com.buspromax.simulatormax;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

public class BusProMaxGlobalApplication extends Application {

    Context context_BusPro;
    public void onCreate() {
        super.onCreate();
        try {
            context_BusPro = this;
        } catch (Exception DirtRide_exception) {

        }

        AdjustConfig configAdjust_DirtRide = new AdjustConfig(this, BusProMaxUtils.DirtRide_ADJUST_TOKEN,
                AdjustConfig.ENVIRONMENT_PRODUCTION);
        Adjust.addSessionCallbackParameter(BusProMaxUtils.DirtRide_User_UUID,
                BusProMaxUtils.createBusPro_ClickID(getApplicationContext()));

        configAdjust_DirtRide.setOnAttributionChangedListener(attribution -> {
            BusProMaxUtils.saveBusPro_AdjustAttribute(getApplicationContext(),
                    attribution.toString());
            BusProMaxUtils.saveBusPro_Campaign(context_BusPro, attribution.campaign);
        });

        configAdjust_DirtRide.setOnDeeplinkResponseListener(deeplink -> {
            BusProMaxUtils.saveBusPro_link(getApplicationContext(), deeplink.toString());
            return false;
        });
        try {
            FirebaseAnalytics.getInstance(context_BusPro)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> {
                        AdjustEvent adjustEvent_DirtRide = new AdjustEvent(BusProMaxUtils.DirtRide_EVENT_TOKEN);
                        adjustEvent_DirtRide.addCallbackParameter(BusProMaxUtils.DirtRide_EVENT_PARAM_KEY,
                                task.getResult());
                        adjustEvent_DirtRide.addCallbackParameter(BusProMaxUtils.DirtRide_User_UUID,
                                BusProMaxUtils.createBusPro_ClickID(getApplicationContext()));
                        Adjust.addSessionCallbackParameter(BusProMaxUtils.DirtRide_PARAM_FIREBASE_INSTANCE_ID,
                                task.getResult());
                        Adjust.trackEvent(adjustEvent_DirtRide);
                        Adjust.sendFirstPackages();

                    });
        } catch (Exception e) {

        }
        configAdjust_DirtRide.setDelayStart(1.5);
        Adjust.onCreate(configAdjust_DirtRide);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    private static final class AdjustLifecycleCallbacks
            implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity,
                                      @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();

        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity,
                                                @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }
}
