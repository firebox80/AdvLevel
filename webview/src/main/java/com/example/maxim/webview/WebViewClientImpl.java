package com.example.maxim.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Android on 29.03.2016.
 */
public class WebViewClientImpl extends WebViewClient {
    private Activity activity = null;
    private Intent intent;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {

//        if(url.indexOf("This is a link") > -1 ) return false;
//        if(url.indexOf("Coffee") > -1 ) return false;

        if(Uri.parse(url).getHost().length() == 0) {
            return false;
        }
// открітие браузера
//        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        webView.getContext().startActivity(intent);
//        открітие в том же вебвью
        webView.loadUrl(url);
        return true;



    }
}
