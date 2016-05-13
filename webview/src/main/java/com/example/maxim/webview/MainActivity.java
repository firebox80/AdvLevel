package com.example.maxim.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String htmlList = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title>Page Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1>This is a Heading</h1>\n" +
            "<p>This is a paragraph.</p>\n" +
            "<ul>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">This is a link</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Coffee</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Tea</a>\n" +
            "</li>\n" +
            "<li>\n" +
            "<a href=\"http://www.w3schools.com\">Milk</a>\n" +
            "</li>\n" +
            "</ul>" +
            "</body>\n" +
            "</html> ";
    private WebView mWebView;
    private WebSettings settings;
    private WebViewClientImpl webViewClient;
    private FrameLayout back;
    private FrameLayout forward;
    private FrameLayout close;
    private FrameLayout refresh;
    private EditText etAddress;
    private String address;
    private ArrayList<String> listWeb = new ArrayList<String>();
    private int index;
    private int size;
    private final String HTTP = "http://";
    private final String HTTPS = "https://";
    private final String STARTLOAD = "file:///android_asset/mypage.html";
    private String tempWeb;
    private String tempHttp = "";
    private String tempHttps = "";
    private String tempProtocol = "";
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = (FrameLayout) findViewById(R.id.back);
        forward = (FrameLayout) findViewById(R.id.forward);
        close = (FrameLayout) findViewById(R.id.close);
        refresh = (FrameLayout) findViewById(R.id.refresh);
        etAddress = (EditText) findViewById(R.id.editText);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setBackgroundColor(Color.parseColor("#3498db"));

        settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        webViewClient = new WebViewClientImpl(this);
        mWebView.loadUrl(STARTLOAD);
        listWeb.add(STARTLOAD);

        mWebView.setWebViewClient(webViewClient);
//     Загрузка файла из папке assets

//        mWebView.loadData("file:///android_asset/mypage.html");
//     Загрузка из интернета необходимі разрешения
//        mWebView.loadUrl("http://developer.alexanderklimov.ru/android");

//  Загрузка private String htmlList
//        mWebView.loadData(htmlList, "text/html", "UTF-8");

        back.setOnClickListener(this);
        forward.setOnClickListener(this);
        close.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                index--;
                if (index >= 0) {
                    etAddress.setText(listWeb.get(index));
                    mWebView.loadUrl(listWeb.get(index));
                } else {
                    index = 0;
                }
                Log.d("t", String.valueOf(index));
                break;
            case R.id.forward:
                if(index + 1 < listWeb.size()) {
                    index++;
                    etAddress.setText(listWeb.get(index));
                    mWebView.loadUrl(listWeb.get(index));
                }
                Log.d("t", String.valueOf(index));
                break;
            case R.id.refresh:

                tempProtocol = "NotProtocol";

                tempWeb = etAddress.getText().toString();
                Log.d("t", tempWeb);

                StringBuffer sbTempWeb = new StringBuffer(tempWeb);



                if (tempWeb.length()>6) {
                    tempProtocol = sbTempWeb.substring(0, 6);

                    if (tempProtocol != HTTP){
                        tempProtocol = sbTempWeb.substring(0, 7);
                    }
                }

                Log.d("t", tempProtocol);

                if (!tempWeb.equals(listWeb.get(index))){

                    if (index + 1 != listWeb.size()){
                        listWeb.remove(index);
                        index--;
                    }
                    index++;
                    switch (tempProtocol){
                        case HTTP:
                        case HTTPS:
                            listWeb.add(index, tempWeb);
                            break;
                        case "NotProtocol":
                        default:
                            listWeb.add(index, HTTP + tempWeb);
                            etAddress.setText(listWeb.get(index));
                            break;
                    }
                }

                Log.d("t", String.valueOf(index));
                mWebView.loadUrl(listWeb.get(index));

                break;
            case R.id.close:

                break;
        }
    }
}
