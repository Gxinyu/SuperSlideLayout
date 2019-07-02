package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.superslide.SlideBackKeeper;
import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseActivity;

public class WebviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webview = findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://www.jianshu.com/p/45a71d72fd16");
        //绑定activity
        SlideBackKeeper.getInstance()
                .createBuilder(this)
                .setSlideEdge(5)
                .attachActivity(this);
    }
}
