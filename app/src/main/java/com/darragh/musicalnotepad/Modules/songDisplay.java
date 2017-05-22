package com.darragh.musicalnotepad.Modules;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.darragh.musicalnotepad.*;
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
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songdisplay);
        final Intent intent = getIntent();
        instantiateView(intent);
        setUpNavigationBar();
        listviewListener(intent);
    }

    private void listviewListener(final Intent intent){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WebViewController.enableWebView(JSONToSongConverter.songFromJSON(dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(intent.getStringExtra("Directory")).child(intent.getStringExtra("Timestamp")).getChildren()),
                        getApplicationContext(),myWebView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setButtons(final Intent intent){
        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareSong(intent);
            }
        });
        Button print = (Button) findViewById(R.id.print);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printSong(intent);
            }
        });
    }

    private void instantiateView(final Intent intent){
        myWebView = (WebView) findViewById(R.id.webView);
        setButtons(intent);
    }

    private void printSong(final Intent intent){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PrintController.printWebView(JSONToSongConverter.songFromJSON(dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren()),
                        getApplicationContext(),myWebView,activity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void shareSong(final Intent intent){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren();
                DialogController.getFriendsList(snap,songDisplay.this,getResources().getString(R.string.users));
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,null,0);
        setActionBarDetails(mDrawerLayout);
    }

    private void setActionBarDetails(DrawerLayout mDrawerLayout){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF18003E")));
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item)||actionBarDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}