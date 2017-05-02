package com.darragh.musicalnotepad.Pagers;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
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
                for(UserProfileDetails x: listDetails){
                    System.out.println(x.userName + " - " + x.UID);
                }

                listView.setAdapter(potentialFriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.friendrequest);
        setUpNavigationBar();
        instantiateView();
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,mDrawerLayout);
    }
}
