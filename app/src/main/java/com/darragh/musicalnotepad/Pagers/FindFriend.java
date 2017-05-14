package com.darragh.musicalnotepad.Pagers;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFriend extends AppCompatActivity{
    private TextView findFriends, searchByEmail;
    private ListView listView;
    private EditText enterEmail;
    private NavigationView navigationBar;
    private ArrayList<UserProfileDetails> listDetails;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                FindFriendListAdapter potentialFriendList = new FindFriendListAdapter(getApplicationContext(),listDetails);
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)+firebaseAuth.getCurrentUser().getUid()
                +"/FriendList/").getChildren();
        for(DataSnapshot data: snap){
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
