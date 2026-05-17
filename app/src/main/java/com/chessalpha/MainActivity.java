package com.chessalpha;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private AdView adView;
    private InterstitialAd interstitialAd;

    // ─── AD UNIT IDs (replace XXX with real unit IDs from AdMob dashboard) ───
    private static final String BANNER_AD_UNIT     = "ca-app-pub-2141482051251808/BANNER_UNIT_ID";
    private static final String INTERSTITIAL_UNIT  = "ca-app-pub-2141482051251808/INTER_UNIT_ID";
    // Note: App ID ca-app-pub-2141482051251808~8680065246 is in AndroidManifest.xml

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep screen on during gameplay
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // ── AdMob init ──
        MobileAds.initialize(this, initStatus -> {});
        loadBannerAd();
        loadInterstitialAd();

        // ── WebView setup ──
        webView = findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // ── JS Bridge ──
        webView.addJavascriptInterface(new JsBridge(), "Android");
        webView.loadUrl("file:///android_asset/game/index.html");
    }

    // ── AdMob Banner ──
    private void loadBannerAd() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(BANNER_AD_UNIT);
        FrameLayout bannerContainer = findViewById(R.id.banner_container);
        bannerContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    // ── AdMob Interstitial ──
    private void loadInterstitialAd() {
        InterstitialAd.load(this, INTERSTITIAL_UNIT, new AdRequest.Builder().build(),
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(InterstitialAd ad) {
                    interstitialAd = ad;
                }
                @Override
                public void onAdFailedToLoad(LoadAdError e) {
                    interstitialAd = null;
                }
            });
    }

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    interstitialAd = null;
                    loadInterstitialAd();
                }
            });
            interstitialAd.show(this);
        }
    }

    // ── JavaScript Bridge ──
    class JsBridge {
        @JavascriptInterface
        public void onGameOver() {
            runOnUiThread(() -> showInterstitial());
        }

        @JavascriptInterface
        public void onNewGame() {
            runOnUiThread(() -> loadInterstitialAd());
        }

        @JavascriptInterface
        public String getDeviceInfo() {
            return android.os.Build.MODEL;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) adView.pause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) adView.destroy();
        if (webView != null) { webView.destroy(); webView = null; }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
