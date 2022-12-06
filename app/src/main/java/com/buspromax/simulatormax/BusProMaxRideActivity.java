package com.buspromax.simulatormax;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class BusProMaxRideActivity extends AppCompatActivity {

    private WebView webView_BusPro;
    LinearLayout errorLayout_BusPro;
    Button BusPro_button_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dirt_webview);
        initDirtRide_webconfig();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initDirtRide_webconfig() {
        webView_BusPro = findViewById(R.id.webviewBusProWeb_id);
        errorLayout_BusPro = findViewById(R.id.webview_errorBusPro_id);
        webView_BusPro.getSettings().setJavaScriptEnabled(true);
        webView_BusPro.getSettings().setUseWideViewPort(true);
        webView_BusPro.getSettings().setLoadWithOverviewMode(true);
        webView_BusPro.getSettings().setDomStorageEnabled(true);
        webView_BusPro.getSettings().setPluginState(WebSettings.PluginState.ON);
        CookieManager.getInstance().setAcceptCookie(true);
        webView_BusPro.setWebChromeClient(new WebChromeClient());
        webView_BusPro.setVisibility(View.VISIBLE);
        webView_BusPro.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                String url_BusPro = request.getUrl().toString();
                if (!url_BusPro.startsWith("http")) {
                    Intent mainIntent_BusPro = new Intent(BusProMaxRideActivity.this, BusProMaxDashActivity.class);
                    startActivity(mainIntent_BusPro);
                    try {
                        Intent busPro_Intent = new Intent(Intent.ACTION_VIEW);
                        busPro_Intent.setData(Uri.parse(url_BusPro));
                        startActivity(busPro_Intent);
                        finish();
                        return;
                    } catch (Exception DirtRide_exception) {
                        finish();
                        return;
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        lets_load_BusPro();
    }
    protected void lets_load_BusPro() {
        if (BusProMaxUtils.hasBusPro_InternetConnected(this)) {
            webView_BusPro.loadUrl(BusProMaxUtils.myBusPro_Link(BusProMaxRideActivity.this));
        } else {
            hadBusProErrorView();
        }
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        webView_BusPro.loadUrl("about:blank");
    }

    public void hadBusProErrorView() {
        errorLayout_BusPro.setVisibility(View.VISIBLE);
        BusPro_button_retry = findViewById(R.id.button_retry_DirtRide);
        BusPro_button_retry.setOnClickListener(view -> {
            errorLayout_BusPro.setVisibility(View.GONE);
            lets_load_BusPro();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        webView_BusPro.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        webView_BusPro.onResume();
    }
}
