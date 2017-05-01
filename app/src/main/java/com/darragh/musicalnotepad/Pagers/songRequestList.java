package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class songRequestList extends AppCompatActivity {
    private String users, profilePicture;
    private Song newSong;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setStrings();
        setContentView(R.layout.songrequestlist);
        setUpNavigationBar();
        instantiateView();
    }

    private void setStrings(){
        users=getResources().getString(R.string.users);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(users+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
                    profilePicture = dataSnapshot.child(users+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void instantiateView(){
        final ListView listView = (ListView) findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ArrayList<Song> songDetails = gatherSongDetails(dataSnapshot);
                SongRequestListAdapter potentialSongList = new SongRequestListAdapter(getApplicationContext(),songDetails,findViewById(android.R.id.content));
                listView.setAdapter(potentialSongList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private ArrayList<Song> gatherSongDetails(DataSnapshot dataSnapshot){
        ArrayList<Song> gatheredSongs = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/SongRequest/").getChildren();
        for(DataSnapshot data: snap){
            Song.sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(dataSnapshot.child(users+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
                newSong = new Song(
                        data.child("timestamp").getValue().toString(),
                        data.child("name").getValue().toString(),
                        data.child("notes").getValue().toString(),
                        data.child("timeSignature").getValue().toString(),
                        data.child("keySignature").getValue().toString(),
                        profilePicture);
            }
            else
            {
                newSong = new Song(
                    data.child("timestamp").getValue().toString(),
                    data.child("name").getValue().toString(),
                    data.child("notes").getValue().toString(),
                    data.child("timeSignature").getValue().toString(),
                    data.child("keySignature").getValue().toString());
            }
            gatheredSongs.add(newSong);
        }
        return gatheredSongs;
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        System.out.println(navigationBar);
        navigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getTitle());
                if(item.getTitle().equals("Record Audio")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else if(item.getTitle().equals("Database Entries")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), DatabaseEntries.class));
                }
//                else if(item.getTitle().equals("User Profile")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
//                }
                else if(item.getTitle().equals("Find Friends")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), FindFriend.class));
                }
                else if(item.getTitle().equals("Friend Requests")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), FriendRequest.class));
                }
                else if(item.getTitle().equals("Friend List")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), FriendList.class));
                }
                else if(item.getTitle().equals("Pending Songs")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), songRequestList.class));
                }
                else if(item.getTitle().equals("Logout")){
                    System.out.println("LOGOUT!!!!!!");
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        //----NOT WORKING AT THE MOMENT----
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }
}
