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

    public static void shareSong(Song _song, ArrayList<String> _UID){
        System.out.println("Send song request...");
        song = _song;
        UID = _UID;
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
                    System.out.println("uid: " + uid);
                    Map<String,Object> map = new HashMap<>();
                    map.put("/users/"+FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()+"/PendingSong/"+ uid +"/",song);
                    map.put("/users/"+ uid +"/SongRequest/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()
                            +"/",song);

                    databaseReference.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

}
