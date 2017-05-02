package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.darragh.musicalnotepad.*;
import com.darragh.musicalnotepad.Modules.WebViewController;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class songDisplay extends AppCompatActivity {
    private WebView myWebView;
    public static DatabaseReference databaseReference;
    private NavigationView navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        final Intent intent = getIntent();
        setContentView(R.layout.songdisplay);
        setUpNavigationBar();
        myWebView = (WebView) findViewById(R.id.webView);
        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), DatabaseEntries.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WebViewController.enableWebView(JSONToSongConverter.songFromJSON(dataSnapshot.child(intent.getStringExtra("Directory")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren()),
                         getApplicationContext(),myWebView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,mDrawerLayout);
    }
}