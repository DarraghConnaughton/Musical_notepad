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

import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.darragh.musicalnotepad.Pagers.MainActivity.firebaseAuth;

public class FriendList extends AppCompatActivity {
    private ArrayList<UserProfileDetails> listDetails;
    private DrawerLayout mDrawerLayout;


    public ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            if(dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("/profilePhoto/").exists()){
                usersFound.add(new UserProfileDetails(
                        dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("username").getValue().toString(),
                        dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("email").getValue().toString(),
                        data.getKey(),
                        dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("/profilePhoto/").getValue().toString()));
            }
            else {
                usersFound.add(new UserProfileDetails(
                        dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("username").getValue().toString(),
                        dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("email").getValue().toString(),
                        data.getKey()));
            }
        }
        return usersFound;
    }

    private void instantiateView(){
        final ListView listView = (ListView) findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listDetails = gatherUsers(dataSnapshot);
                FriendListAdapter FriendList = new FriendListAdapter(getApplicationContext(),listDetails);
                listView.setAdapter(FriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setUpNavigationBar(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationBar = (NavigationView) findViewById(R.id.navigationBar);
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.friendlist);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        instantiateView();

        setUpNavigationBar();


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
    }
}
