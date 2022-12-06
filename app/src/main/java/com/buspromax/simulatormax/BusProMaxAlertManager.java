package com.buspromax.simulatormax;

import android.content.Context;
import android.content.SharedPreferences;
class BusProMaxAlertManager {

    private static final String busProMaxSTRING_PREFS = "rate_prefs";

    public static void fUpdateFirebaseTokenBus(Context context, String token) {
        final SharedPreferences prefs = context.getSharedPreferences(busProMaxSTRING_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString("FirebaseToken", token).apply();
    }

}

