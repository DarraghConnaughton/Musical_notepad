package com.darragh.musicalnotepad.Pagers;

import android.net.Uri;

/**
 * Created by darragh on 26/04/17.
 */

public class UserProfileDetails {
    public String userName, emailAddress, UID;
    public Uri profileImageUri;

    public UserProfileDetails(String username, String email, String uid){
        this.userName = username;
        this.emailAddress = email;
        this.UID = uid;
    }
}
