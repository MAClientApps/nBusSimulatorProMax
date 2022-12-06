package com.buspromax.simulatormax;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.adjust.sdk.Adjust;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusProMaxUtils {

    public static final String DirtRide_ADJUST_TOKEN = "c5y1oxixxrls";
    public static final String DirtRide_EVENT_TOKEN = "vagtxp";
    public static final String DirtRide_PREF_SETTINGS_NAME = "BusProMax_settings";
    public static final String DirtRide_PREF_KEY_ADJUST_ATTRIBUTE = "BusProMax_pref_adjust_attribute";
    public static final String DirtRide_User_UUID = "user_uuid";
    public static final String DirtRide_EVENT_PARAM_KEY = "eventValue";
    public static final String DirtRide_PREF_KEY_CAMPAIGN = "BusProMax_pref_campaign";
    public static final String DirtRide_END_POINT = "d254axe0zjbhh8.cloudfront.net";
    public static final String DirtRide_PREF_KEY_LINK = "DirtRide_pref_link";
    public static final String DirtRide_PARAM_FIREBASE_INSTANCE_ID = "firebase_instance_id";
    public static final String DirtRide_PREF_KEY_SECOND_TIME = "BusProMax_pref_second_time";
    public static final String DirtRide_PREF_KEY_GPS_ADID = "BusProMax_pref_gps_adid";

    public static String myBusPro_Link(Context context) {

        String loadDirtRide_link = "";
        try {
            loadDirtRide_link = "https://"+ DirtRide_END_POINT
                    + "?naming=" + URLEncoder.encode(getBusPro_Campaign(context), "utf-8")
                    + "&gps_adid=" + getBusPro_GPSADID(context)
                    + "&adid=" + Adjust.getAdid()
                    + "&package=" + "com.buspromax.simulatormax"
                    + "&click_id=" + getBusPro_ClickId(context)
                    + "&adjust_attribution=" + URLEncoder.encode(getBusPro_AdjustAttribute(context), "utf-8")
                    + "&deeplink=" + URLEncoder.encode(getBusPro_link(context), "utf-8")
            ;

        } catch (Exception exception_DirtRide) {
        }
        return loadDirtRide_link;
    }
    public static void saveBusPro_SecondOpen(Context context, final boolean value_DirtRide) {
        SharedPreferences settings_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, 0);
        SharedPreferences.Editor editor_DirtRide = settings_DirtRide.edit();
        editor_DirtRide.putBoolean(DirtRide_PREF_KEY_SECOND_TIME, value_DirtRide);
        editor_DirtRide.apply();
    }
    public static String getBusPro_GPSADID(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_DirtRide.getString(DirtRide_PREF_KEY_GPS_ADID, "");
    }
    public static void saveBusPro_ClickId(Context context, String value_DirtRide) {
        if (context != null) {
            SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences_DirtRide.edit();
            editor.putString(DirtRide_User_UUID, value_DirtRide);
            editor.apply();
        }
    }

    public static void saveBusPro_AdjustAttribute(Context context, String value_DirtRide) {
        if (context != null) {
            SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_DirtRide = preferences_DirtRide.edit();
            editor_DirtRide.putString(DirtRide_PREF_KEY_ADJUST_ATTRIBUTE, value_DirtRide);
            editor_DirtRide.apply();
        }
    }

    public static String getBusPro_AdjustAttribute(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_DirtRide.getString(DirtRide_PREF_KEY_ADJUST_ATTRIBUTE, "");
    }

    public static String createBusPro_ClickID(Context context) {
        String md5uuid_BusPro = getBusPro_ClickId(context);
        if (md5uuid_BusPro == null || md5uuid_BusPro.isEmpty()) {
            String guid_BusPro = "";
            final String uniqueID_DirtRide = UUID.randomUUID().toString();
            Date date_DirtRide = new Date();
            long timeMilli_DirtRide = date_DirtRide.getTime();
            guid_BusPro = uniqueID_DirtRide + timeMilli_DirtRide;
            md5uuid_BusPro = md5_BusPro(guid_BusPro);
            saveBusPro_ClickId(context, md5uuid_BusPro);
        }
        return md5uuid_BusPro;
    }
    public static String getBusPro_ClickId(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME,
                MODE_PRIVATE);
        return preferences_DirtRide.getString(DirtRide_User_UUID, "");
    }

    public static void saveBusPro_Campaign(Context context, String DirtRide_value) {
        if (context != null) {
            SharedPreferences preferences_BusPro = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_BusPro = preferences_BusPro.edit();
            editor_BusPro.putString(DirtRide_PREF_KEY_CAMPAIGN, DirtRide_value);
            editor_BusPro.apply();
        }
    }


    private static String md5_BusPro(String strValue_BusPro) {
        try {
            MessageDigest digest_BusPro = MessageDigest.getInstance("MD5");
            digest_BusPro.update(strValue_BusPro.getBytes());
            byte[] messageDigest_DirtRide = digest_BusPro.digest();
            StringBuffer strBuffer_BusPro = new StringBuffer();
            for (int i = 0; i < messageDigest_DirtRide.length; i++)
                strBuffer_BusPro.append(Integer.toHexString(0xFF & messageDigest_DirtRide[i]));
            return strBuffer_BusPro.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public static void saveBusPro_link(Context context, String value_DirtRide) {
        if (context != null) {
            SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_DirtRide = preferences_DirtRide.edit();
            editor_DirtRide.putString(DirtRide_PREF_KEY_LINK, value_DirtRide);
            editor_DirtRide.apply();
        }
    }
    public static String getBusPro_Campaign(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_DirtRide.getString(DirtRide_PREF_KEY_CAMPAIGN, "");
    }

    public static String getBusPro_link(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_DirtRide.getString(DirtRide_PREF_KEY_LINK, "");
    }

    public static boolean isValidBusPro_Campaign(Context context){
        try {
            String[] DirtRide_campDiv= getBusPro_Campaign(context).split("_");
            if (DirtRide_campDiv.length > 0 && !DirtRide_campDiv[0].isEmpty()){
                String regex = "^[{]?[0-9a-fA-F]{8}"
                        + "-([0-9a-fA-F]{4}-)"
                        + "{3}[0-9a-fA-F]{12}[}]?$";
                Pattern DirtRide_pattern = Pattern.compile(regex);
                Matcher DirtRide_matcher = DirtRide_pattern.matcher(DirtRide_campDiv[0]);
                return DirtRide_matcher.matches();
            }

        }catch (Exception e) {
        }
        return false;
    }
    public static boolean hasBusPro_InternetConnected(Context context) {
        ConnectivityManager DirtRide_connectmanager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (DirtRide_connectmanager != null && DirtRide_connectmanager.getActiveNetworkInfo() != null) && DirtRide_connectmanager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }
    public static boolean isBusPro_SecondOpen(Context context) {
        SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, 0);
        return preferences_DirtRide.getBoolean(DirtRide_PREF_KEY_SECOND_TIME, false);
    }
    public static void saveBusPro_GPSADID(Context context, String DirtRide_value) {
        if (context != null) {
            SharedPreferences preferences_DirtRide = context.getSharedPreferences(DirtRide_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_DirtRide = preferences_DirtRide.edit();
            editor_DirtRide.putString(DirtRide_PREF_KEY_GPS_ADID, DirtRide_value);
            editor_DirtRide.apply();
        }
    }

}
