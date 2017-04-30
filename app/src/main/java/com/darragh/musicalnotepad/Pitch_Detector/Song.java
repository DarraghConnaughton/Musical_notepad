package com.darragh.musicalnotepad.Pitch_Detector;

public class Song {
    private static String name, notes, timestamp, timeSignature, keySignature, L, UID, profilePhoto;
    public static  String sender;

    public Song(){}
    public Song(String sTimestamp){
        timestamp=sTimestamp;
        name="";
        notes="";
        timeSignature="";
        keySignature="Emin";
        L="1/8";
    }
    public Song(String sTimestamp, String sName){
        timestamp=sTimestamp;
        name=sName;
        notes="";
        timeSignature="";
        keySignature="Emin";
        L="1/8";
    }
    public Song(String sTimestamp, String sName, String sNotes){
        timestamp = sTimestamp;
        name = sName;
        notes = sNotes;
        timeSignature="";
        keySignature="Emin";
        L="1/8";
    }
    public Song(String sTimestamp, String sName, String sNotes, String sTimesignature){
        timestamp = sTimestamp;
        name = sName;
        notes = sNotes;
        timeSignature = sTimesignature;
        keySignature="Emin";
        L="1/8";
    }
    public Song(String sTimestamp, String sName, String sNotes, String sTimesignature, String sKeysignature){
        timestamp = sTimestamp;
        name = sName;
        notes = sNotes;
        timeSignature = sTimesignature;
        keySignature=sKeysignature;
        L="1/8";
    }
    public Song(String sTimestamp, String sName, String sNotes, String sTimesignature, String sKeysignature, String profile){
        timestamp = sTimestamp;
        name = sName;
        notes = sNotes;
        timeSignature = sTimesignature;
        keySignature=sKeysignature;
        L="1/8";
        profilePhoto=profile;
    }
    public void setName(String sName){
        name = sName;
    }
    public void setNotes(String sNotes){
        notes = sNotes;
    }
    public void setTimestamp(String sTimestamp){
        timestamp = sTimestamp;
    }
    public void setKeySignature(String sKeySignature){
        keySignature = sKeySignature;
    }
    public void setTimeSignature(String sTimeSignature){
        timeSignature = sTimeSignature;
    }
    public void setL(String sL){
        L = sL;
    }
    public void setUID(String uid){
        UID = uid;
    }
    public void setProfilePhoto(String profile){
        profilePhoto = profile;
    }

    public void printDetails(){
        System.out.println(name + "\n" + notes + "\n" + timestamp + "\n" + timeSignature + "\n" + keySignature + "\n" + L);
    }
    public String getProfilePhoto(){return profilePhoto;}
    public String getTimestamp(){
        return timestamp;
    }
    public String getName(){
        return name;
    }
    public String getNotes(){
        return notes;
    }
    public String getKeySignature(){
        return keySignature;
    }
    public String getTimeSignature(){
        return timeSignature;
    }
    public String getL(){
        return L;
    }
    public String getSender(){
        return sender;
    }
    public String getUID(){
        return UID;
    }
}
