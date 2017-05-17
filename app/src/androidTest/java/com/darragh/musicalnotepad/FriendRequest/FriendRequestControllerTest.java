package com.darragh.musicalnotepad.FriendRequest;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.darragh.musicalnotepad.Login_Register.Register;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FriendRequestControllerTest {
    private String username,UID,currentUID,mainDirectory;

    @Rule
    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(
            Register.class);

    @Before
    public void initValidString() {
        UID = "testUID";
        username = "testUser";
        currentUID = "currentUID";
        mainDirectory= "/testHierarchy/";
    }

    @Test
    public void sendFriendRequest() throws Exception {
        FriendRequestController.sendFriendRequest(UID,username,currentUID,mainDirectory);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(dataSnapshot snap: dataSnapshot.child(mainDirectory).getChildren()){
//                    Sy;
//
//                }
                System.out.println("-----------------------------");

                System.out.println(dataSnapshot.child(mainDirectory).getKey());

                System.out.println("-----------------------------");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Test
//    public void removeFriendRequest() throws Exception {
//
//    }
//
//    @Test
//    public void acceptFriendRequest() throws Exception {
//
//    }

}