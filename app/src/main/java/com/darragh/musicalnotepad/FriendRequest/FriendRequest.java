package com.darragh.musicalnotepad.FriendRequest;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.darragh.musicalnotepad.Modules.NavigationView_Details;
import com.darragh.musicalnotepad.Objects.UserProfileDetails;
import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRequest extends AppCompatActivity{
    private ListView listView;
    private ArrayList<UserProfileDetails> listDetails;
    private NavigationView navigationBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendRequest/").getChildren();
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
        listView = (ListView) findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listDetails = gatherUsers(dataSnapshot);
                FriendRequestListAdapter potentialFriendList = new FriendRequestListAdapter(getApplicationContext(),listDetails);
                potentialFriendList.notifyDataSetChanged();
                listView.setAdapter(potentialFriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequest);
        setUpNavigationBar();
        instantiateView();
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
