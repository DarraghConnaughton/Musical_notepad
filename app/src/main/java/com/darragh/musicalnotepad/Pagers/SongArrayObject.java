package com.darragh.musicalnotepad.Pagers;

import java.util.ArrayList;

/**
 * Created by darragh on 30/04/17.
 */

public class SongArrayObject {
    private ArrayList<String> listEntriesName = new ArrayList<>();
    private ArrayList<String> listEntriesID = new ArrayList<>();
    private ArrayList<String> listEntriesTimesignature = new ArrayList<>();
    private ArrayList<String> listEntriesKeysignature = new ArrayList<>();
    private ArrayList<String> listEntriesTimestamp = new ArrayList<>();
    private ArrayList<String> listEntriesProfilePhoto = new ArrayList<>();

    public SongArrayObject(ArrayList<String> _listEntriesName,ArrayList<String> _listEntriesID,
                            ArrayList<String> _listEntriesTimesignature, ArrayList<String> _listEntriesKeysignature,
                            ArrayList<String> _listEntriesTimestamp){
        this.listEntriesName = _listEntriesName;
        this.listEntriesID = _listEntriesID;
        this.listEntriesTimesignature = _listEntriesTimesignature;
        this.listEntriesKeysignature = _listEntriesKeysignature;
        this.listEntriesTimestamp = _listEntriesTimestamp;
    }
    public void setProfilePhoto(ArrayList<String> _listEntriesProfilePhoto){
        listEntriesProfilePhoto = _listEntriesProfilePhoto;
    }
    public ArrayList<String> getListEntriesName(){
        return listEntriesName;
    }
    public ArrayList<String> getListEntriesID(){
        return listEntriesID;
    }
    public ArrayList<String> getListEntriesTimesignature(){
        return listEntriesTimesignature;
    }
    public ArrayList<String> getListEntriesKeysignature(){
        return listEntriesKeysignature;
    }
    public ArrayList<String> getListEntriesTimestamp(){
        return listEntriesTimestamp;
    }
    public ArrayList<String> getListEntriesProfilePhoto(){
        return listEntriesProfilePhoto;
    }
}
