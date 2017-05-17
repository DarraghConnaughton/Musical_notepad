package com.darragh.musicalnotepad.FriendFinder;

import com.darragh.musicalnotepad.Objects.UserProfileDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class FriendFinderController {
    public static ArrayList<UserProfileDetails> FindPotentialFriends(DataSnapshot dataSnapshot, String searchQuery, String users){
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).getChildren();
        for(DataSnapshot data: snap){
            if(notMe(data.getKey()) && notFriend(dataSnapshot,data.getKey().toString(),users)){
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

    private static boolean notMe(String uid){
        return !uid.equals(FirebaseAuth.getInstance().getCurrentUser()
                .getUid());
    }

    private static boolean containsSubstring(String searchQuery, String databaseString){
        return (databaseString.toLowerCase().contains(searchQuery.toLowerCase()));
    }

    private static boolean notFriend(DataSnapshot dataSnapshot,String currentEntry, String users){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users+firebaseAuth.getCurrentUser().getUid()
                +"/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            if(data.getKey().equals(currentEntry)){
                return false;
            }
        }
        return true;
    }
}
