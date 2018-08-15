package com.seoul.ddroad.board;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.seoul.ddroad.R;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class BoardActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mWebView = (WebView) findViewById(R.id.webView);
       // mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.loadUrl("http://www.pois.co.kr/mobile/login.do");

        mWebView.loadUrl("http://daum.net"); // 접속 URL
        //mWebView.setWebChromeClient(new WebChromeClient());  https://hyobase-572cb.firebaseapp.com/

    }

    private class WebViewClientClass extends WebChromeClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
