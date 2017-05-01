package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindFriend extends AppCompatActivity{
    private TextView findFriends, searchByEmail;
    private ListView listView;
    private EditText enterEmail;
    private NavigationView navigationBar;
    private ArrayList<UserProfileDetails> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.findfriends);
        setUpNavigationBar();
        instantiateView();
    }

    private void instantiateView(){
        findFriends = (TextView) findViewById(R.id.findFriends);
        searchByEmail = (TextView) findViewById(R.id.editEmail);
        listView = (ListView) findViewById(R.id.listView);
        setEditText();
    }

    private void setEditText(){
        enterEmail = (EditText) findViewById(R.id.searchBar);
        enterEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                populateListView(enterEmail.getText().toString());
                System.out.println(enterEmail.getText());
                return true;
            }
        });
    }



    private void populateListView(final String searchQuery){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listDetails = gatherUsers(dataSnapshot, searchQuery);
                FriendListAdapter potentialFriendList = new FriendListAdapter(getApplicationContext(),listDetails);
                listView.setAdapter(potentialFriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private boolean notMe(String uid){
        return !uid.equals(FirebaseAuth.getInstance().getCurrentUser()
                .getUid());
    }

    private boolean notFriend(DataSnapshot dataSnapshot,String currentEntry){
        System.out.println("currentEntry: " + currentEntry);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)+firebaseAuth.getCurrentUser().getUid()
                +"/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            System.out.println("data.getValue(): " + data.getValue().toString());
            if(data.getKey().equals(currentEntry)){
               return false;
            }
        }
        return true;
    }

    private ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot, String searchQuery){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).getChildren();
        for(DataSnapshot data: snap){
            if(notMe(data.getKey()) && notFriend(dataSnapshot,data.getKey().toString())){
                if(containsSubstring(searchQuery,data.child("username").getValue().toString()) || containsSubstring(searchQuery,data.child("email").getValue().toString())){
                    if(data.child("/profilePhoto/").exists()){
                        usersFound.add(new UserProfileDetails(
                                data.child("username").getValue().toString(),
                                data.child("email").getValue().toString(),
                                data.getKey().toString(),
                                data.child("/profilePhoto/").getValue().toString()));
                    }
                    else {
                        usersFound.add(new UserProfileDetails(
                                data.child("username").getValue().toString(),
                                data.child("email").getValue().toString(),
                                data.getKey().toString()));
                    }
                }
            }

        }
        return usersFound;
    }

    private boolean containsSubstring(String searchQuery, String databaseString){
        return (databaseString.toLowerCase().contains(searchQuery.toLowerCase()));
    }

    private void setProfilePicture(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        View headView = navigationBar.inflateHeaderView(R.layout.userprofile);
        ImageView profileImage = (ImageView) headView.findViewById(R.id.profilePicture);
        System.out.println("Profile image: " + profileImage);
        for(UserInfo profile : firebaseUser.getProviderData()){
            Picasso.with(this).load(profile.getPhotoUrl()+"?sz=500").into(profileImage);
        }
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        setProfilePicture();
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
                    firebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        //----NOT WORKING AT THE MOMENT----
        actionBarDrawerToggle.syncState();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }

}
