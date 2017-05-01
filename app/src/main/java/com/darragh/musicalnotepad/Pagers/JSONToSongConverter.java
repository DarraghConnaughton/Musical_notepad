package com.darragh.musicalnotepad.Pagers;

import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.google.firebase.database.DataSnapshot;

public class JSONToSongConverter {
    public static Song songFromJSON(Iterable<DataSnapshot> dataSnapshot){
        Song song = new Song();
        for(DataSnapshot snap: dataSnapshot){
            System.out.println(snap + " - " +snap.getValue());
            switch(snap.getKey()){
                case "name" :
                    song.setName((String)snap.getValue());
                    break;
                case "keySignature" :
                    song.setKeySignature((String)snap.getValue());
                    break;
                case "l" :
                    song.setL((String)snap.getValue());
                    break;
                case "notes" :
                    song.setNotes((String)snap.getValue());
                    break;
                case "timeSignature" :
                    song.setTimeSignature((String)snap.getValue());
                    break;
                case "timestamp" :
                    song.setTimestamp((String)snap.getValue());
                    break;
                case "profilePhoto":
                    song.setProfilePhoto((String)snap.getValue());
                    break;
                case "uid":
                    song.setUID((String)snap.getValue());
                    break;
            }
        }
        return song;
    }
}
