package com.darragh.musicalnotepad.FriendList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.darragh.musicalnotepad.Login_Register.SignIn;
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

public class FriendList extends AppCompatActivity {
    private ArrayList<UserProfileDetails> listDetails;
    private NavigationView navigationBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private void instantiateView(){
        final ListView listView = (ListView) findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listDetails = FriendListController.gatherFriendList(dataSnapshot,getResources().getString(R.string.users));
                FriendListAdapter FriendList = new FriendListAdapter(getApplicationContext(),listDetails);
                listView.setAdapter(FriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setUpNavigationBar(){
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,null,0);
        setActionBarDetails(mDrawerLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        instantiateView();
        setUpNavigationBar();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
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

