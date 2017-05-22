package com.darragh.musicalnotepad.FriendList;

import com.darragh.musicalnotepad.Objects.UserProfileDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class FriendListController {
    public static ArrayList<UserProfileDetails> gatherFriendList(DataSnapshot dataSnapshot, String users){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            if(dataSnapshot.child(users).child(data.getKey()).child("/profilePhoto/").exists()){
                usersFound.add(new UserProfileDetails(
                        dataSnapshot.child(users).child(data.getKey()).child("username").getValue().toString(),
                        dataSnapshot.child(users).child(data.getKey()).child("email").getValue().toString(),
                        data.getKey(),
                        dataSnapshot.child(users).child(data.getKey()).child("profilePhoto").getValue().toString()));
            }
            else {
                usersFound.add(new UserProfileDetails(
                        dataSnapshot.child(users).child(data.getKey()).child("username").getValue().toString(),
                        dataSnapshot.child(users).child(data.getKey()).child("email").getValue().toString(),
                        data.getKey()));
            }
        }
        return usersFound;
    }

}
