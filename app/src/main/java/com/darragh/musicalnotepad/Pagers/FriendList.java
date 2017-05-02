package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

public class FriendList extends AppCompatActivity {
    private ArrayList<UserProfileDetails> listDetails;
    private NavigationView navigationBar;


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
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,mDrawerLayout);
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
