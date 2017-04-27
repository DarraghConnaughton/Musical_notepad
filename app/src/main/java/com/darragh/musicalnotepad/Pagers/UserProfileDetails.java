package com.darragh.musicalnotepad.Pagers;

/**
 * Created by darragh on 26/04/17.
 */

public class UserProfileDetails {
    public String userName, emailAddress, UID, profileImageUri;

    public UserProfileDetails(String username, String email, String uid){
        this.userName = username;
        this.emailAddress = email;
        this.UID = uid;
    }

    public UserProfileDetails(String username, String email, String uid, String profile){
        this.userName = username;
        this.emailAddress = email;
        this.UID = uid;
        this.profileImageUri = profile;
    }
}
