package com.darragh.musicalnotepad.SongRequest;

import com.darragh.musicalnotepad.Objects.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

class SongRequestController {
    private static Song newSong;

    public static ArrayList<Song> gatherSongDetails(DataSnapshot dataSnapshot, String users, String profilePicture){
        ArrayList<Song> gatheredSongs = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/SongRequest/").getChildren();
        for(DataSnapshot data: snap){
            if(!data.child("profilePhoto").getValue().equals(null)){
                newSong = new Song(
                        data.child("timestamp").getValue().toString(),
                        data.child("name").getValue().toString(),
                        data.child("notes").getValue().toString(),
                        data.child("timeSignature").getValue().toString(),
                        data.child("keySignature").getValue().toString(),
                        data.child("profilePhoto").getValue().toString());
                newSong.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
            else {
                newSong = new Song(
                        data.child("timestamp").getValue().toString(),
                        data.child("name").getValue().toString(),
                        data.child("notes").getValue().toString(),
                        data.child("timeSignature").getValue().toString(),
                        data.child("keySignature").getValue().toString());
                newSong.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
            gatheredSongs.add(newSong);
        }
        return gatheredSongs;
    }

}
