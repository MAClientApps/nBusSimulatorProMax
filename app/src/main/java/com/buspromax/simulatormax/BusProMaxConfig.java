package com.buspromax.simulatormax;


public class BusProMaxConfig {

    // Your URL including https:// or http:// prefix and including www. or any required subdomain (e.g., "https://www.example.org")
    public static String _HOME_URL_BUS = "https://www.cargames.com/games/Bus-Parking/index.html";

    // Set to "false" to disable the progress spinner/loading spinner
    public static final boolean _ACTIVATE_PROGRESS_BAR_BUS = true;

    // Set a customized UserAgent for WebView URL requests (or leave it empty to use the default Android UserAgent)
    public static final String _USER_AGENT_BUS = "";

    // Set to "true" if you want to extend URL request by the system language like ?webview_language=LANGUAGE CODE (e.g., ?webview_language=EN for English users)
    public static final boolean _APPEND_LANG_CODE_BUS = false;

    // Set to (0) to open external links in-app by default; (1) to ALWAYS open in a new tab (an additional in-app browser); (2) to ALWAYS open in another browser
    public static final int _EXTERNAL_URL_HANDLING_OPTIONS_BUS = 0;

    // Set to "true" to open links with attributes (_blank, _self) in new a tab by default
    // Set to "true" to open links with attributes (_blank, _self) in new a tab by default; Set to "false" to open them in-app;
        // NOTE: is overridden by EXTERNAL_URL_HANDLING_OPTIONS (options 1 or 2) if it is also an external link
    public static final boolean _OPEN_SPECIAL_URLS_IN_NEW_TAB_BUS = true;

    // Set to "true" to clear the WebView cache on each app startup and do not use cached versions of your web app/website
    public static final boolean _CLEAR_CACHE_ON_STARTUP_BUS = false;

    //Set to "true" to use local "assets/index.html" file instead of URL
    public static final boolean _USE_LOCAL_HTML_FOLDER_BUS = false;

    //Set to "true" to enable deep linking for App Links & Push (take a look at the documentation for further information)
    public static final boolean _IS_DEEP_LINKING_ENABLED_BUS = true;

    //Set to "true" to open the notification deep linking URLs in the system browser instead of your app
    public static final boolean _OPEN_NOTIFICATION_URLS_IN_SYSTEM_BROWSER_BUS = false;

    //Set to "true" for black status bar text; Set to "false" for white status bar text; Use 'colorPrimaryDark' in style.xml to choose the status bar background color
    static boolean _blackStatusBarText_BUS = false;

    //Set to "true" to prevent the device from going into sleep while the app is active
    public static final boolean _PREVENT_SLEEP_BUS = false;

    //Set to "true" to enable navigation by swiping left or right to move back or forward a page
    public static final boolean _ENABLE_SWIPE_NAVIGATE_BUS = false;

    //Set to "true" to enable swiping down to refresh the page
    public static final boolean _ENABLE__PULL_REFRESH_BUS = false;

    //Set to "true" to close the app by pressing the hardware back button (instead of going back to the last page)
    public static final boolean _EXIT_APP_BY_BACK_BUTTON_ALWAYS_BUS = false;
    //Set to "true" to close the app by pressing the hardware back button if the user is on the home page (which does not allow going to a prior page)
    public static final boolean _EXIT_APP_BY_BACK_BUTTON_HOMEPAGE_BUS = true;

    //Set to "true" to activate Firebase Push (download the google-services.json file and replace the existing one via Mac Finder/Windows Explorer)
    public static final boolean _FIREBASE_PUSH_ENABLED_BUS = false;

    //Set to "true" if you want to extend URL request by ?firebase_push_id=XYZ (set the OneSignal IDs in the build.gradle file)
    public static final boolean _FIREBASE_PUSH_ENHANCE_WEB_VIEW_URL_BUS = false;

    //Allow normal URL clicks to increment SHOW_AD_AFTER_X
    public static final boolean _INCREMENT_WITH_REDIRECTS_BUS = true;


}
