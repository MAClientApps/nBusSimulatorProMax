package com.buspromax.simulatormax;

import static com.buspromax.simulatormax.BusProMaxConfig._ACTIVATE_PROGRESS_BAR_BUS;
import static com.buspromax.simulatormax.BusProMaxConfig._ENABLE_SWIPE_NAVIGATE_BUS;
import static com.buspromax.simulatormax.BusProMaxConfig._ENABLE__PULL_REFRESH_BUS;
import static com.buspromax.simulatormax.BusProMaxConfig._INCREMENT_WITH_REDIRECTS_BUS;
import static com.buspromax.simulatormax.BusProMaxConfig._OPEN_SPECIAL_URLS_IN_NEW_TAB_BUS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.BuildConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import androidx.core.content.FileProvider;

public class BusProMaxMainActivity extends AppCompatActivity{

    public static final int _PERMISSION_REQUEST_CODE_BUS = 9786;
    public static final String _WRITE_SUCCESS_BUS = "Text written to the NFC tag successfully!";
    public static final String _WRITE_ERROR_BUS = "Error during writing, is the NFC tag close enough to your device?";
    private static final String _INDEX_FILE_BUS = "file:///android_asset/piano/index.html";
    private static final int _CODE_AUDIO_CHOOSER_BUS = 4643;
    private BusProMaxCustomWebView _webView_BUS;
    private View _offlineLayout_BUS;
    public static final int _REQUEST_CODE_QR_SCAN_BUS = 5757;
    SwipeRefreshLayout _mySwipeRefreshLayout_BUS;
    public ProgressBar _progressBar_BUS;
    private String deepLinkingURL_BUS;
    private static final String TAG = "MainActivity_BusMax";
    private String _BUS_mCM, _BUS_mVM_;
    private ValueCallback<Uri[]> _BUS_mUMA_;
    private final static int _BUS_FCR_ = 1;
    public String _BUS_hotsart_;
//    private String successUrl_BusMax = "", failUrl_BusMax = "";
    private boolean _isNotificationURL_BUS = false;
//    private boolean extendediap_BusMax = true;
    public String _uuid_BUS = "";
    public static Context _Context;
    private String _firebaseUserToken_BUS = "";
    private boolean _isRedirected_BUS = false;
    static  long _TimeStamp_BUS =0;
    static  boolean _isInBackGround_BUS =false;
    private boolean _readModeNFC_BUS = false;
    private boolean _writeModeNFC_BUS = false;
    private String _textToWriteNFC_BUS = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _Context = this;
        _uuid_BUS = Settings.System.getString(super.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (BusProMaxConfig._blackStatusBarText_BUS) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){

        }
        if (BusProMaxConfig._PREVENT_SLEEP_BUS) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_pro_main);

        boolean NFCenabled_BusMax = false;
        if (NFCenabled_BusMax) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.NFC},
                        _PERMISSION_REQUEST_CODE_BUS);
            } else {
                initNfc_BusMax();
            }
        }

        if (BusProMaxConfig._FIREBASE_PUSH_ENABLED_BUS) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        _firebaseUserToken_BUS = token;
                        BusProMaxAlertManager.fUpdateFirebaseTokenBus(BusProMaxMainActivity.this, token);
                        Log.d(TAG, token);
                    });
        }
        StrictMode.ThreadPolicy threadPolicy_BusMax = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy_BusMax);


        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && (intent.getData().getScheme().equals("http"))) {
            Uri data = intent.getData();
            List<String> pathSegments = data.getPathSegments();
            if (pathSegments.size() > 0) {
                deepLinkingURL_BUS = pathSegments.get(0).substring(5);
                String fulldeeplinkingurl = data.getPath().toString();
                fulldeeplinkingurl = fulldeeplinkingurl.replace("/link=", "");
                deepLinkingURL_BUS = fulldeeplinkingurl;
            }
        } else if (intent != null && intent.getData() != null && (intent.getData().getScheme().equals("https"))) {
            Uri data = intent.getData();
            List<String> pathSegments = data.getPathSegments();
            if (pathSegments.size() > 0) {
                deepLinkingURL_BUS = pathSegments.get(0).substring(5);
                String fulldeeplinkingurl = data.getPath().toString();
                fulldeeplinkingurl = fulldeeplinkingurl.replace("/link=", "");
                deepLinkingURL_BUS = fulldeeplinkingurl;
            }
        }

        if (intent != null) {
            Bundle extras = getIntent().getExtras();
            String URL = null;
            if (extras != null) {
                URL = extras.getString("ONESIGNAL_URL");
            }
            if (URL != null && !URL.equalsIgnoreCase("")) {
                _isNotificationURL_BUS = true;
                deepLinkingURL_BUS = URL;
            } else _isNotificationURL_BUS = false;
        }

        _webView_BUS = findViewById(R.id.webView);
        _webView_BUS.setGestureDetector(new GestureDetector(new CustomeGestureDetector_BusMax()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        _mySwipeRefreshLayout_BUS = findViewById(R.id.swipeContainer);

        if (!_ENABLE__PULL_REFRESH_BUS) {
            _mySwipeRefreshLayout_BUS.setEnabled(false);

        }
        _mySwipeRefreshLayout_BUS.setOnRefreshListener(
                () -> {
                    if (_ENABLE__PULL_REFRESH_BUS) {
                        _webView_BUS.reload();

                    }
                    _mySwipeRefreshLayout_BUS.setRefreshing(false);

                }
        );


        _offlineLayout_BUS = findViewById(R.id.offline_layout);
        this.findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(R.color.launchLoadingSignBackground));
        _progressBar_BUS = findViewById(R.id.progressBar);
        _progressBar_BUS.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        final Button tryAgainButton = findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(view -> {
            System.out.println("Try again!");
            _webView_BUS.setVisibility(View.GONE);
            loadMainUrl_BusMax();

        });

        _webView_BUS.setWebViewClient(new MyWebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String url) {
                    _webView_BUS.setVisibility(View.GONE);
                    _offlineLayout_BUS.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!_isRedirected_BUS) {

                    if (url.startsWith("mailto:")) {
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                        return true;
                    }
                    if (url.startsWith("share:") || url.contains("api.whatsapp.com")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("whatsapp:")) {
                        Intent i = new Intent();
                        i.setPackage("com.whatsapp");
                        i.setAction(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("geo:") || url.contains("maps:")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("market:")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("maps.app.goo.gl")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.contains("maps.google.com")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("intent:")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("tel:")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("sms:")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                    if (url.startsWith("play.google.com")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    }

                    if (_OPEN_SPECIAL_URLS_IN_NEW_TAB_BUS) {
                        WebView.HitTestResult result = view.getHitTestResult();
                        String data = result.getExtra();
                        Log.i(TAG, " data :" + data);
                        if ((data != null && data.endsWith("#")) || url.startsWith("newtab:")) {
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
                            CustomTabsIntent customTabsIntent = builder.build();
                            String finalUrl = url;

                            if (url.startsWith("newtab:")) {
                                finalUrl = url.substring(7);
                            }

                            customTabsIntent.launchUrl(BusProMaxMainActivity.this, Uri.parse(finalUrl));
                            _webView_BUS.stopLoading();
                            return false;
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return false;
            }
        });
        _webView_BUS.getSettings().setSupportMultipleWindows(true);
        _webView_BUS.getSettings().setUseWideViewPort(true);
        _webView_BUS.getSettings().setLoadWithOverviewMode(true);
        _webView_BUS.getSettings().setDomStorageEnabled(true);
        _webView_BUS.getSettings().setPluginState(WebSettings.PluginState.ON);


        _webView_BUS.setWebChromeClient(new MyWebChromeClient_BusMax() {
//            private Handler notificationHandler;

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

                Bundle extras = getIntent().getExtras();
                String URL = null;
                String launchUrl = null;
                if (extras != null )
                {
                    URL = extras.getString("ONESIGNAL_URL");
                    launchUrl=extras.getString("openURL");
                }
                if (URL != null && !URL.equalsIgnoreCase("")) {
                    _isNotificationURL_BUS = true;
                    deepLinkingURL_BUS = URL;
                } else _isNotificationURL_BUS = false;

                if(launchUrl!=null)
                {
                    openInExternalBrowser(launchUrl);
                }
                Log.i(TAG, " LOG24 " + deepLinkingURL_BUS);

                WebView.HitTestResult hitTestResult_BusMax = view.getHitTestResult();
                String data = hitTestResult_BusMax.getExtra();
                if (URLUtil.isValidUrl(data)) {
                    _BUS_hotsart_ = Uri.parse(data).getHost();
                   // if (!(hostpart.contains(Config.HOST) || data.startsWith(Config.HOST))) {
                        if (BusProMaxConfig._EXTERNAL_URL_HANDLING_OPTIONS_BUS == 1) {
                            // open in a new tab (additional in-app browser)
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(BusProMaxMainActivity.this, Uri.parse(data));
                            _webView_BUS.stopLoading();
                            return true;
                        } else if (BusProMaxConfig._EXTERNAL_URL_HANDLING_OPTIONS_BUS == 2) {
                            // open in a new browser
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                            startActivity(i);
                            return true;
                        }
                   // }
                }

                if (!_OPEN_SPECIAL_URLS_IN_NEW_TAB_BUS) {
                    Log.i(TAG, "if ");
                    Context context = view.getContext();
                    if (data == null) {
                        Log.i(TAG, "else true ");
                        WebView newWebView = new WebView(view.getContext());
                        newWebView.setWebChromeClient(new MyWebChromeClient_BusMax());
                        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                        transport.setWebView(newWebView);
                        resultMsg.sendToTarget();
                        return true;
                    } else {
                        if (URLUtil.isValidUrl(data)) {
                            _webView_BUS.loadUrl(data);
                        }
                    }
                } else {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
                    CustomTabsIntent customTabsIntent = builder.build();
                    Log.i("TAG", " data " + data);
//                    String url = "";
                    WebView newWebView = new WebView(view.getContext());
                    newWebView.setWebChromeClient(new WebChromeClient());
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();
                }
                Log.i("TAG", " running this main activity ");
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i(TAG, " onJsalert");
                return super.onJsAlert(view, url, message, result);
            }

            @SuppressLint("InlinedApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {


                if (_BUS_mUMA_ != null) {
                    _BUS_mUMA_.onReceiveValue(null);
                }
                _BUS_mUMA_ = filePathCallback;

                if (Arrays.asList(fileChooserParams.getAcceptTypes()).contains("audio/*")) {
                    Intent chooserIntent = fileChooserParams.createIntent();
                    startActivityForResult(chooserIntent, _CODE_AUDIO_CHOOSER_BUS);
                    return true;
                }

                Intent takePictureIntent_BusMax = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent_BusMax.resolveActivity(BusProMaxMainActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile_BusMax();
                        takePictureIntent_BusMax.putExtra("PhotoPath", _BUS_mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        _BUS_mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent_BusMax.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(BusProMaxMainActivity.this, getPackageName() + ".provider", photoFile));
                    } else {
                        takePictureIntent_BusMax = null;
                    }
                }
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(BusProMaxMainActivity.this.getPackageManager()) != null) {
                    File videoFile = null;
                    try {
                        videoFile = createVideoFile_BusMax();
                        takeVideoIntent.putExtra("PhotoPath", _BUS_mVM_);
                    } catch (IOException ex) {
                        Log.e(TAG, "Video file creation failed", ex);
                    }
                    if (videoFile != null) {
                        _BUS_mVM_ = "file:" + videoFile.getAbsolutePath();
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(BusProMaxMainActivity.this, getPackageName() + ".provider", videoFile));
                    } else {
                        takeVideoIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                contentSelectionIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/* video/*");

                String[] mimeTypes = {"text/csv", "text/comma-separated-values", "application/pdf", "image/*", "video/*", "*/*"};
                contentSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                Intent[] intentArray;
                if (takePictureIntent_BusMax != null && takeVideoIntent != null) {
                    intentArray = new Intent[]{takePictureIntent_BusMax, takeVideoIntent};
                } else if (takePictureIntent_BusMax != null) {
                    intentArray = new Intent[]{takePictureIntent_BusMax};
                } else if (takeVideoIntent != null) {
                    intentArray = new Intent[]{takeVideoIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Upload");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, _BUS_FCR_);
                return true;
            }

        });


        _webView_BUS.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        registerForContextMenu(_webView_BUS);

        final WebSettings webSettings = _webView_BUS.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (BusProMaxConfig._CLEAR_CACHE_ON_STARTUP_BUS) {
            webSettings.setAppCacheEnabled(false);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(_webView_BUS, true);
        }
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (!BusProMaxConfig._USER_AGENT_BUS.isEmpty()) {
            webSettings.setUserAgentString(BusProMaxConfig._USER_AGENT_BUS);
        }

        if (BusProMaxConfig._CLEAR_CACHE_ON_STARTUP_BUS) {
            _webView_BUS.clearCache(true);
        }

        if (BusProMaxConfig._USE_LOCAL_HTML_FOLDER_BUS) {
            loadLocal_BusMax(_INDEX_FILE_BUS);
        } else if (isConnectedNetwork()) {
            if (BusProMaxConfig._USE_LOCAL_HTML_FOLDER_BUS) {
                loadLocal_BusMax(_INDEX_FILE_BUS);
            } else {
                loadMainUrl_BusMax();
            }
        } else {
            loadLocal_BusMax(_INDEX_FILE_BUS);
        }
        if (!connectedNow) {
            checkInternetConnection_BusMax();
        }



        if(getIntent().getExtras()!=null)
        {
            String openurl=getIntent().getExtras().getString("openURL");
            if(openurl!=null)
            {
                openInExternalBrowser(openurl);
            }

        }

    }

    private void openInExternalBrowser(String launchUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(launchUrl));
        startActivity(browserIntent);
    }

    private static boolean connectedNow = false;

    private void checkInternetConnection_BusMax() {
        //auto reload every 5s
        class AutoRec extends TimerTask {
            public void run() {
                runOnUiThread(() -> {

                    if (!isConnectedNetwork()) {
                        connectedNow = false;
                        _offlineLayout_BUS.setVisibility(View.VISIBLE);
                        System.out.println("attempting reconnect");
                        _webView_BUS.setVisibility(View.GONE);
                        loadMainUrl_BusMax();
                        Log.d("", "reconnect");
                    } else {
                        if (!connectedNow) {
                            Log.d("", "connected");
                            System.out.println("Try again!");
                            _webView_BUS.setVisibility(View.GONE);
                            loadMainUrl_BusMax();
                            connectedNow = true;
                            if (timer != null) timer.cancel();

                        }
                    }
                });

            }
        }
        timer.schedule(new AutoRec(), 0, 5000);
    }


//    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
//        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
//    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        _webView_BUS.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _webView_BUS.restoreState(savedInstanceState);
    }

    private void loadLocal_BusMax(String path) {
        _webView_BUS.loadUrl(path);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final WebView.HitTestResult webViewHitTestResult = _webView_BUS.getHitTestResult();

        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {


        }
    }

    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE_BusMax = 242;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE_BusMax) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        Uri[] results = null;
        Uri uri;
        if (requestCode == _BUS_FCR_) {
            if (resultCode == Activity.RESULT_OK) {
                if (_BUS_mUMA_ == null) {
                    return;
                }
                if (intent == null || intent.getData() == null) {

                    if (intent != null && intent.getClipData() != null) {

                        int count = intent.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        results = new Uri[intent.getClipData().getItemCount()];
                        for (int i = 0; i < count; i++) {
                            uri = intent.getClipData().getItemAt(i).getUri();
                            // results = new Uri[]{Uri.parse(mCM)};
                            results[i] = uri;

                        }
                    }

                    if (_BUS_mCM != null) {
                        File file = new File(Uri.parse(_BUS_mCM).getPath());
                        if (file.length() > 0)
                            results = new Uri[]{Uri.parse(_BUS_mCM)};
                        else
                            file.delete();
                    }
                    if (_BUS_mVM_ != null) {
                        File file = new File(Uri.parse(_BUS_mVM_).getPath());
                        if (file.length() > 0)
                            results = new Uri[]{Uri.parse(_BUS_mVM_)};
                        else
                            file.delete();
                    }

                } else {
                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    } else {
                        if (intent.getClipData() != null) {
                            final int numSelectedFiles = intent.getClipData().getItemCount();
                            results = new Uri[numSelectedFiles];
                            for (int i = 0; i < numSelectedFiles; i++) {
                                results[i] = intent.getClipData().getItemAt(i).getUri();
                            }
                        }

                    }
                }
            } else {
                if (_BUS_mCM != null) {
                    File file = new File(Uri.parse(_BUS_mCM).getPath());
                    if (file != null) file.delete();
                }
                if (_BUS_mVM_ != null) {
                    File file = new File(Uri.parse(_BUS_mVM_).getPath());
                    if (file != null) file.delete();
                }
            }
            _BUS_mUMA_.onReceiveValue(results);
            _BUS_mUMA_ = null;
        } else if (requestCode == _CODE_AUDIO_CHOOSER_BUS) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null && intent.getData() != null) {
                    results = new Uri[]{intent.getData()};
                }
            }
            _BUS_mUMA_.onReceiveValue(results);
            _BUS_mUMA_ = null;
        } else if (requestCode == _REQUEST_CODE_QR_SCAN_BUS) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    String result = intent.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                    if (result != null && URLUtil.isValidUrl(result)) {
                        _webView_BUS.loadUrl(result);
                    }
                }
            }
        }
    }

    private File createImageFile_BusMax() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "";
        File mediaStorageDir = getCacheDir();
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + "WebView" + " directory");
                return null;
            }
        }
        return File.createTempFile(
                imageFileName,
                ".jpg",
                mediaStorageDir
        );
    }

    private File createVideoFile_BusMax() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "VID_" + timeStamp + "";
        File mediaStorageDir = getCacheDir();

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + "WebView" + " directory");
                return null;
            }
        }
        return File.createTempFile(
                imageFileName,
                ".mp4",
                mediaStorageDir
        );
    }

    @Override
    public void onBackPressed() {
        if (BusProMaxConfig._EXIT_APP_BY_BACK_BUTTON_ALWAYS_BUS) {
            super.onBackPressed();
        } else if (_webView_BUS.canGoBack()) {
            _webView_BUS.goBack();
        }
        else if (BusProMaxConfig._EXIT_APP_BY_BACK_BUTTON_HOMEPAGE_BUS) {
            super.onBackPressed();
        }

    }

    private void customCSS_BusMax() {
        try {
            InputStream inputStream_BusMax = getAssets().open("custom.css");
            byte[] cssbuffer = new byte[inputStream_BusMax.available()];
            inputStream_BusMax.read(cssbuffer);
            inputStream_BusMax.close();

            String encodedcss = Base64.encodeToString(cssbuffer, Base64.NO_WRAP);
            if (!TextUtils.isEmpty(encodedcss)) {
                Log.d("css", "Custom CSS loaded");
                _webView_BUS.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        "style.innerHTML = window.atob('" + encodedcss + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMainUrl_BusMax() {

        if (isConnectedNetwork()) {
            _offlineLayout_BUS.setVisibility(View.GONE);

            if (BusProMaxConfig._IS_DEEP_LINKING_ENABLED_BUS && deepLinkingURL_BUS != null && !deepLinkingURL_BUS.isEmpty()) {
                Log.i(TAG, " deepLinkingURL " + deepLinkingURL_BUS);
                if (_isNotificationURL_BUS && BusProMaxConfig._OPEN_NOTIFICATION_URLS_IN_SYSTEM_BROWSER_BUS && URLUtil.isValidUrl(deepLinkingURL_BUS)) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkingURL_BUS)));
                    deepLinkingURL_BUS = null;
                } else if (URLUtil.isValidUrl(deepLinkingURL_BUS)) {
                    _webView_BUS.loadUrl(deepLinkingURL_BUS);
                    return;
                } else {
                    Toast.makeText(this, "URL is not valid", Toast.LENGTH_SHORT).show();
                }
            }
            String urlExt = "";
            String urlExt2 = "";
            String language_BusMax = "";
            if (BusProMaxConfig._APPEND_LANG_CODE_BUS) {
                language_BusMax = Locale.getDefault().getLanguage().toUpperCase();
                language_BusMax = "?webview_language=" + language_BusMax;
            } else {
                language_BusMax = "";
            }
            String urlToLoad_BusMax = BusProMaxConfig._HOME_URL_BUS + language_BusMax;

            if (BusProMaxConfig._FIREBASE_PUSH_ENABLED_BUS) {
                if (BusProMaxConfig._FIREBASE_PUSH_ENHANCE_WEB_VIEW_URL_BUS) {
                    String userID2 = _firebaseUserToken_BUS;
                    if (!userID2.isEmpty()) {
                        if (urlToLoad_BusMax.contains("?") || urlExt.contains("?")) {
                            urlExt2 = String.format("%sfirebase_push_id=%s", "&", userID2);
                        } else {
                            urlExt2 = String.format("%sfirebase_push_id=%s", "?", userID2);
                        }
                    } else {
                        urlExt2 = "";
                    }
                }
            }
            if (BusProMaxConfig._USE_LOCAL_HTML_FOLDER_BUS) {
                loadLocal_BusMax(_INDEX_FILE_BUS);
            } else {
                _webView_BUS.loadUrl(urlToLoad_BusMax + urlExt + urlExt2);
            }
        }
    }

    public boolean isConnectedNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == _PERMISSION_REQUEST_CODE_BUS) {
            initNfc_BusMax();
        }

    }

    @Override
    public void onPause() {
        _isInBackGround_BUS =true;
        _TimeStamp_BUS = Calendar.getInstance().getTimeInMillis();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();

        _isInBackGround_BUS =false;
        _TimeStamp_BUS = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(BusProMaxMainActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, BusProMaxMainActivity.this,
                        1001);
                if (dialog != null) {
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (ConnectionResult.SERVICE_INVALID == resultCode) {

                            }
                        }
                    });
                    return false;
                }
            }
            Toast.makeText(this, "Please make sure to install the latest version of the Google Play Services app.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private Handler notificationHandler;

    Timer timer = new Timer();

    @SuppressWarnings("SpellCheckingInspection")
    private class MyWebViewClient extends WebViewClient {

        MyWebViewClient() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!_isRedirected_BUS || _INCREMENT_WITH_REDIRECTS_BUS) {
                super.onPageStarted(view, url, favicon);

            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!_isRedirected_BUS) {
                setTitle(view.getTitle());
                customCSS_BusMax();
                super.onPageFinished(view, url);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!_isRedirected_BUS) {
                _BUS_hotsart_ = Uri.parse(url).getHost();

                if (isConnectedNetwork()) {

                 if (url.startsWith("sendlocalpushmsg://push.send")) {
                        _webView_BUS.stopLoading();
                        if (_webView_BUS.canGoBack()) {
                            _webView_BUS.goBack();
                        }
                        sendNotification_BusMax(url);
                    } else if (url.startsWith("sendlocalpushmsg://push.send.cancel") && notificationHandler != null) {
                        _webView_BUS.stopLoading();
                        if (_webView_BUS.canGoBack()) {
                            _webView_BUS.goBack();
                        }
                        notificationHandler.removeCallbacksAndMessages(null);
                        notificationHandler = null;
                    } else if (url.startsWith("get-uuid://")) {
                        _webView_BUS.loadUrl("javascript: var uuid = '" + _uuid_BUS + "';");
                        return true;
                    } else if (url.startsWith("reset://")) {
                        WebSettings webSettings = _webView_BUS.getSettings();
                        webSettings.setAppCacheEnabled(false);
                        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                        _webView_BUS.clearCache(true);
                        Toast.makeText(BusProMaxMainActivity.this, "App reset was successful.", Toast.LENGTH_LONG).show();
                        loadMainUrl_BusMax();
                        return true;
                    }

                    else if (url.startsWith("readnfc://")) {
                        _readModeNFC_BUS = true;
                        _writeModeNFC_BUS = false;
                        return true;
                    } else if (url.startsWith("writenfc://")) {
                        _writeModeNFC_BUS = true;
                        _readModeNFC_BUS = false;

                        _textToWriteNFC_BUS = url.substring(url.indexOf("=") + 1, url.length());

                        return true;
                    } else if (url.startsWith("spinneron://")) {
                        _progressBar_BUS.setVisibility(View.VISIBLE);
                        return true;
                    } else if (url.startsWith("spinneroff://")) {
                        _progressBar_BUS.setVisibility(View.GONE);
                        return true;
                    }
                    else if (url.startsWith("getappversion://")) {
                         _webView_BUS.loadUrl("javascript: var versionNumber = '" + BuildConfig.VERSION_NAME + "';" +
                    "var bundleNumber  = '" + BuildConfig.VERSION_CODE + "';");
                        return true;

                    } else if (url.startsWith("shareapp://")) {
                        Log.e(TAG, url);
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";

                        if (url.contains("sharetext?=")) {
                            String key_share_text = "sharetext?=";
                            int firstIndex = url.lastIndexOf(key_share_text);
                            shareMessage = url.substring(firstIndex + key_share_text.length());
                        }

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "Share the app"));
                        return true;
                    }
                } else if (!isConnectedNetwork()) {
                    Log.e(TAG, "isConnectedNetwork: False");
                    _offlineLayout_BUS.setVisibility(View.VISIBLE);
                    return true;
                }

                if (_BUS_hotsart_.contains("whatsapp.com")) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    final int newDocumentFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? Intent.FLAG_ACTIVITY_NEW_DOCUMENT : Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | newDocumentFlag | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent);
                }
                if (((BusProMaxConfig._EXTERNAL_URL_HANDLING_OPTIONS_BUS != 0)
                        && !(url).startsWith("file://") && (!BusProMaxConfig._USE_LOCAL_HTML_FOLDER_BUS
                        || !(url).startsWith("file://"))) && URLUtil.isValidUrl(url)) {

                    if (BusProMaxConfig._EXTERNAL_URL_HANDLING_OPTIONS_BUS == 1) {
                        // open in a new tab (additional in-app browser)
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(BusProMaxMainActivity.this, Uri.parse(url));
                        _webView_BUS.stopLoading();
                        return true;
                    } else if (BusProMaxConfig._EXTERNAL_URL_HANDLING_OPTIONS_BUS == 2) {
                        // open in a new browser
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return false;
        }
    }


    private void sendNotification_BusMax(String url) {
        final int secondsDelayed = Integer.parseInt(url.split("=")[1]);

        final String[] contentDetails = (url.substring((url.indexOf("msg!") + 4), url.length())).split("&!#");
        final String message = contentDetails[0].replaceAll("%20", " ");
        final String title = contentDetails[1].replaceAll("%20", " ");

        final Notification.Builder builder = getNotificationBuilder_BusMax(title, message);

        final Notification notification = builder.build();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationHandler = null;
        notificationHandler = new Handler();
        notificationHandler.postDelayed((Runnable) () -> {
            notificationManager.notify(0, notification);
            notificationHandler = null;
        }, secondsDelayed * 1000);
    }


    private Notification.Builder getNotificationBuilder_BusMax(String title, String message) {

        createNotificationChannel_BusMax();
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(BusProMaxMainActivity.this, getString(R.string.BusMax_notification_channel_id));
        } else {
            builder = new Notification.Builder(BusProMaxMainActivity.this);
        }

        Intent intent = new Intent(BusProMaxMainActivity.this, BusProMaxMainActivity.class);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(BusProMaxMainActivity.this, 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        return builder;
    }



    private void createNotificationChannel_BusMax() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.BusMax_notification_channel_name);
            String description = getString(R.string.BusMax_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.BusMax_notification_channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private class CustomeGestureDetector_BusMax extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (_ENABLE_SWIPE_NAVIGATE_BUS) {
                if (e1 == null || e2 == null) return false;
                if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
                else {
                    try { // right to left swipe .. go to next page
                        if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 800) {
                            if (_webView_BUS.canGoBack()) {
                                _webView_BUS.goBack();
                            }
                            return true;
                        } //left to right swipe .. go to prev page
                        else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 800) {
                            //do your stuff
                            if (_webView_BUS.canGoForward()) {
                                _webView_BUS.goForward();
                            }
                            return true;
                        }
                    } catch (Exception e) { // nothing
                    }
                    return false;
                }
            }
            return false;
        }
    }

    private class MyWebChromeClient_BusMax extends WebChromeClient {

        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyWebChromeClient_BusMax() {
        }


        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

        boolean progressBarActive = false;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, "progress " + newProgress);

            //Activate progress bar if this is a new redirect
            if (_ACTIVATE_PROGRESS_BAR_BUS && !progressBarActive) {
                _progressBar_BUS.setVisibility(View.VISIBLE);
                progressBarActive = true;
            }

            _isRedirected_BUS = true;

            if (newProgress >= 80 && _ACTIVATE_PROGRESS_BAR_BUS && progressBarActive) {
                _progressBar_BUS.setVisibility(View.GONE);
                progressBarActive = false;
            }

            if (newProgress == 100) {
                _isRedirected_BUS = false;
                _webView_BUS.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            request.grant(request.getResources());
        }

    }


    // nfc

    private void initNfc_BusMax() {
        // TODO: add guard here to prevent setting up NFC if customer is not using NFC
        // e.g if (!Config.NFC) { return false; }

        // nfc
        NfcAdapter nfcAdapter_BusMax = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter_BusMax == null) {
            //Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
        readFromIntent_BusMax(getIntent());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        IntentFilter[] writeTagFilters = new IntentFilter[]{tagDetected};

    }


    private void readFromIntent_BusMax(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];

                }
            }
            read_BusMax(msgs);
        }
    }

    private void read_BusMax(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

            _webView_BUS.loadUrl("javascript: readNFCResult('" + text + "');");


        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        TextView textView = new TextView(this);
        textView.setPadding(16,16,16,16);
        textView.setTextColor(Color.BLUE);
        textView.setText("read : " + text);

    }

    private void write_BusMax(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord_BusMax(text)};
        NdefMessage message = new NdefMessage(records);
        writeData_BusMax(tag , message);

    }

    public void writeData_BusMax(Tag tag, NdefMessage message)  {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null) {
                    // Let's try to format the Tag in NDEF
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                        toast(_WRITE_SUCCESS_BUS);
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                    toast(_WRITE_SUCCESS_BUS);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                toast("write error : " + e.getMessage());
            }
        }
    }

    private NdefRecord createRecord_BusMax(String text)
            throws UnsupportedEncodingException {

        if (text.startsWith("VCARD")){

            String nameVcard = "BEGIN:" +
                    text.replace('_', '\n').replace("%20", " ")
                    + '\n' + "END:VCARD";

            byte[] uriField = nameVcard.getBytes(StandardCharsets.US_ASCII);
            byte[] payload = new byte[uriField.length + 1];              //add 1 for the URI Prefix
            //payload[0] = 0x01;                                      //prefixes http://www. to the URI
            System.arraycopy(uriField, 0, payload, 1, uriField.length);  //appends URI to payload

            NdefRecord nfcRecord = new NdefRecord(
                    NdefRecord.TNF_MIME_MEDIA, "text/vcard".getBytes(), new byte[0], payload);

            return nfcRecord;
        }


                String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(!_readModeNFC_BUS && !_writeModeNFC_BUS){
            return;
        }
        super.onNewIntent(intent);
        setIntent(intent);
        if(_readModeNFC_BUS){
            readFromIntent_BusMax(intent);
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag myTag_BusMax = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            toast("tag detected : " + myTag_BusMax.toString());



            try {
                if (_writeModeNFC_BUS) {
                    write_BusMax(_textToWriteNFC_BUS, myTag_BusMax);
                }
            } catch (IOException | FormatException e) {
                e.printStackTrace();
                Toast.makeText(this, _WRITE_ERROR_BUS, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
