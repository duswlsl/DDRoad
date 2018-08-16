package com.seoul.ddroad.board;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.seoul.ddroad.R;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class BoardActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://guitarhyo.freehongs.net"); // 접속 URL

        mWebView.addJavascriptInterface(new JavascriptTest(), "android");
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new MyWebChrome());
    }

    class JavascriptTest {
        @JavascriptInterface
        public String getChartData(){
            StringBuffer buffer=new StringBuffer();
            buffer.append("[");
            for(int i=0; i<14; i++){
                buffer.append("["+i+","+Math.sin(i)+"]");
                if(i<13) buffer.append(",");
            }
            buffer.append("]");
            return buffer.toString();
        }
    }

    class MyWebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast t=Toast.makeText(BoardActivity.this, url, Toast.LENGTH_SHORT) ;
            t.show();
            return true;
        }
    }

    class MyWebChrome extends WebChromeClient{

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast t=Toast.makeText(BoardActivity.this, message, Toast.LENGTH_SHORT);
            t.show();
            result.confirm();
            return true;
        }
    }

}
