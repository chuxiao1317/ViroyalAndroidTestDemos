package com.viroyal.sloth.app;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sloth.core.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 内嵌WebView来加载url的一个Activity，但是不支持自定义script，
 * 这样的Activity可以自己继承本activity来实现
 * <p>
 * Created by yu.zai on 2015/12/30.
 */
@Deprecated
public class ShowWebActivity extends SlothActivity {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_HEADERS = "headers";

    protected ProgressBar mProgressBar;

    protected WebView mWebView;

    protected ImageView no_net_image;

    protected View mNetworkErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_view);

        String url = getIntent().getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        String title = getIntent().getStringExtra(EXTRA_TITLE);
//        Slog.d(LOG_TAG, "title=" + title + "  url=" + url);
//        if (!TextUtils.isEmpty(title)) {
//            setActionBarTitle(title);
//        }
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_web);
        mWebView = (WebView) findViewById(R.id.web_view);
        no_net_image = (ImageView) findViewById(R.id.no_net_image);
        mNetworkErrorView = findViewById(R.id.network_error);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                mNetworkErrorView.setVisibility(View.VISIBLE);
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Slog.d(TAG, "onReceivedTitle=" + title);
//                 if (!TextUtils.isEmpty(title)) {
//                     setActionBarTitle(title);
//                 }
            }

            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                //Slog.d(LOG_TAG, "onProgressChanged=" + progress);

                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    //String title = mWebView.getTitle();
                    //if (!TextUtils.isEmpty(title)) {
                    //    setActionBarTitle(title);
                    //}
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });

        Headers headers = (Headers) getIntent().getSerializableExtra(EXTRA_HEADERS);
        if (headers != null) {
            mWebView.loadUrl(url, headers.mHeader);
        } else {
            mWebView.loadUrl(url);
        }
    }

    /**
     *
     */
    //@Override
    //public void onActionBackPress() {
    //    onBackPressed();
    //}
    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    public static class Headers implements Serializable {
        public Map<String, String> mHeader = new HashMap<String, String>();

        public void add(String key, String value) {
            mHeader.put(key, value);
        }
    }
}