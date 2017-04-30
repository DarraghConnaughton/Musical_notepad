package com.darragh.musicalnotepad.Pagers;

import android.support.v7.app.AppCompatActivity;
import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongSharer extends AppCompatActivity {
    private static Song song;
    private static ArrayList<String> UID;
    private static String profilePhoto;

    public static void shareSong(Song _song, ArrayList<String> _UID, String profile){
        System.out.println("Send song request...");
        song = _song;
        UID = _UID;
        profilePhoto = profile;
        sendSongRequest();
    }

    private static void sendSongRequest(){
        System.out.println("Send song request...");
        System.out.println(song.getProfilePhoto());
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(String uid: UID){
                    System.out.println("CURRENT USER: : " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    song.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if(profilePhoto!=null){
                        song.setProfilePhoto(profilePhoto);
                    }
                    System.out.println("Song.UID: "+ song.getUID());
                    Map<String,Object> map = new HashMap<>();
                    map.put("/users/"+FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()+"/PendingSong/"+ song.getTimestamp() +"/",song);
                    map.put("/users/"+ uid +"/SongRequest/"+ song.getTimestamp()
                            +"/",song);

                    databaseReference.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

}
