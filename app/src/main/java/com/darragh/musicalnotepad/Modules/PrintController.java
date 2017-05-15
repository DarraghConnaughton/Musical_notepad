package com.darragh.musicalnotepad.Modules;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;

@TargetApi(21)
public class PrintController {
    public static Song printWebView(final Song displaySong, Context context, WebView webView, Activity activity){
        final JavaScriptInterface jsInterface = new JavaScriptInterface(context);
        jsInterface.setSong(displaySong);
        webView.addJavascriptInterface(jsInterface, "Android");
        webView.loadUrl("file:///android_asset/webDisplay.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter = webView.createPrintDocumentAdapter("Printable");
        printManager.print(displaySong.getName(),printDocumentAdapter,null);
        return displaySong;
    }
}
