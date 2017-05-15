package com.darragh.musicalnotepad.Login_Register;

public class User {

    public String username,email,profilePhoto;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}