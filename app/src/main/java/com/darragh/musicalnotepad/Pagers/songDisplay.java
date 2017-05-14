package com.darragh.musicalnotepad.Pagers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.darragh.musicalnotepad.*;
import com.darragh.musicalnotepad.Modules.PrintController;
import com.darragh.musicalnotepad.Modules.WebViewController;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class songDisplay extends AppCompatActivity {
    private WebView myWebView;
    private static String profilePhoto;
    public static DatabaseReference databaseReference;
    private NavigationView navigationBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songdisplay);
        Firebase.setAndroidContext(this);
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
                WebViewController.enableWebView(JSONToSongConverter.songFromJSON(dataSnapshot.child(intent.getStringExtra("Directory")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren()),
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

    private void setProfilePhoto(DataSnapshot data){
        if(data.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
            profilePhoto = data.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").getValue().toString();
        }
    }

    public ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot){
        setProfilePhoto(dataSnapshot);
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            usersFound.add(new UserProfileDetails(
                    dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("username").getValue().toString(),
                    dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("email").getValue().toString(),
                    data.getKey()));
        }
        return usersFound;
    }

    private void getFriendsList(final Iterable<DataSnapshot> snap){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                onCreateDialog(gatherUsers(dataSnapshot),snap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    public void onCreateDialog(final ArrayList<UserProfileDetails> listDetails, final Iterable<DataSnapshot> snap) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(songDisplay.this);
        String[] entries = getEmailAddresses(listDetails);
        builder.setTitle(R.string.dialog_title)
                .setMultiChoiceItems(entries, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SongSharer.shareSong(JSONToSongConverter.songFromJSON(snap),selectedFriends(mSelectedItems,listDetails),profilePhoto);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }

    private ArrayList<String> selectedFriends(ArrayList<Integer> mSelectedItems,ArrayList<UserProfileDetails> listDetails){
        ArrayList<String> selectedFriends = new ArrayList<>();

        for(int x: mSelectedItems){
            selectedFriends.add(listDetails.get(x).UID);
        }
        return selectedFriends;

    }

    private String[] getEmailAddresses(ArrayList<UserProfileDetails> listDetails){
        String[] emails = new String[listDetails.size()];
        for(int i=0; i<listDetails.size(); i++){
            emails[i] = listDetails.get(i).emailAddress;
        }
        return emails;
    }

    private void printSong(final Intent intent){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PrintController.printWebView(JSONToSongConverter.songFromJSON(dataSnapshot.child(intent.getStringExtra("Directory")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren()),
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
                Iterable<DataSnapshot> snap = dataSnapshot.child(intent.getStringExtra("Directory")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").child(intent.getStringExtra("Timestamp")).getChildren();
                getFriendsList(snap);
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
}