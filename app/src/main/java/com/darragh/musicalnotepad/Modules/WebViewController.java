package com.darragh.musicalnotepad.Modules;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;

public class WebViewController extends Activity {

    public static void disableWebView(WebView myWebView){
        myWebView.loadUrl("about:blank");
    }

    public static Song enableWebView(final Song displaySong, Context context, WebView webView){
        final JavaScriptInterface jsInterface = new JavaScriptInterface(context);
        jsInterface.setSong(displaySong);
        webView.addJavascriptInterface(jsInterface, "Android");
        webView.loadUrl("file:///android_asset/webDisplay.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return displaySong;
    }
}
