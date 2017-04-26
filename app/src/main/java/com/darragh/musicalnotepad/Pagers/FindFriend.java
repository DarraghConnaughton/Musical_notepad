package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<UserProfileDetails> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.findfriends);
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

    private ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot, String searchQuery){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).getChildren();
        for(DataSnapshot data: snap){
//            System.out.println(data.getKey());
            if(containsSubstring(searchQuery,data.child("username").getValue().toString())
                    || containsSubstring(searchQuery,data.child("email").getValue().toString())){
                System.out.println(data.child("username").getValue());
                System.out.println(data.child("email").getValue());
                usersFound.add(new UserProfileDetails(data.child("username").getValue().toString(),
                        data.child("email").getValue().toString(),
                        data.getKey().toString()));
            }
        }
        return usersFound;
    }

    private boolean containsSubstring(String searchQuery, String databaseString){
        return (databaseString.toLowerCase().contains(searchQuery.toLowerCase()));
    }

}
