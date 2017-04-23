package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.darragh.musicalnotepad.Modules.JavaScriptInterface;
import com.darragh.musicalnotepad.Modules.WebViewController;
import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by darragh on 01/04/17.
 * */

public class songDisplay extends AppCompatActivity {
    private WebView myWebView;
    private View view;
    public static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        final Intent intent = getIntent();
        setContentView(R.layout.songdisplay);
        myWebView = (WebView) findViewById(R.id.webView);

        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PagerControl.class);
                intent.putExtra("previousPage","SongDisplay");
                finish();
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("--------------------------------");
                System.out.println("--------------------------------");
                System.out.println("--------------------------------");
//                System.out.println(myWebView.getRootView());
                WebViewController.enableWebView(songFromJSON(dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren()),
                         getApplicationContext(),myWebView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
//        enableWebView(intent.getStringExtra("Timestamp"));
    }

    public Song songFromJSON(Iterable<DataSnapshot> dataSnapshot){
        Song song = new Song();
        for(DataSnapshot snap: dataSnapshot){
            System.out.println(snap + " - " +snap.getValue());
            switch(snap.getKey()){
                case "name" :
                    song.setName((String)snap.getValue());
                    break;
                case "keySignature" :
                    song.setKeySignature((String)snap.getValue());
                    break;
                case "l" :
                    song.setL((String)snap.getValue());
                    break;
                case "notes" :
                    song.setNotes((String)snap.getValue());
                    break;
                case "timeSignature" :
                    song.setTimeSignature((String)snap.getValue());
                    break;
                case "timestamp" :
                    song.setTimestamp((String)snap.getValue());
                    break;
            }
        }
        System.out.println("SONG!");
        song.printDetails();
        return song;
    }

    public void disableWebView(){
        myWebView.loadUrl("about:blank");
    }
    public void enableWebView(final String timestamp){
        myWebView = (WebView) findViewById(R.id.webview);
        final JavaScriptInterface jsInterface = new JavaScriptInterface(getApplicationContext());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(myWebView);
                jsInterface.setSong(songFromJSON(dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(timestamp).getChildren()));
                myWebView.addJavascriptInterface(jsInterface, "Android");
                myWebView.loadUrl("file:///android_asset/webDisplay.html");
                WebSettings webSettings = myWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
}