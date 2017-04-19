package com.darragh.musicalnotepad;

/**
 * Created by darragh on 12/03/17.
 */

public class DatabaseEntry {
    private static String songName, songNotes;

    public DatabaseEntry(){}

    public DatabaseEntry(String name){
        songName=name;
    }
    public DatabaseEntry(String name, String notes){
        songName=name;
        songNotes=notes;
    }
}
