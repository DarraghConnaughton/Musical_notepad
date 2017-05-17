package com.darragh.musicalnotepad.SongRequest;

import com.darragh.musicalnotepad.Modules.JSONToSongConverter;
import com.darragh.musicalnotepad.Objects.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

class SongRequestListController {
    public static void addSongToSongList(final String timeStamp, final String user){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Map<String,Object> map = new HashMap<>();
                Song sentSong = JSONToSongConverter.songFromJSON(dataSnapshot.child(user+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/SongRequest/"+timeStamp+"/").getChildren());

                map.put(user+ FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()+"/songId/"+timeStamp+"/",sentSong);
                databaseReference.updateChildren(map);
                RemoveSongRequest(timeStamp, databaseReference, sentSong.getUID(),user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    public static void RemoveSongRequest(String timeStamp,DatabaseReference databaseReference, String UID, final String user){
        databaseReference.child(user+UID+"/PendingSong/"+timeStamp)
                .setValue(null);
        databaseReference.child(user+FirebaseAuth.getInstance()
                .getCurrentUser().getUid()+"/SongRequest/"+timeStamp).setValue(null);
    }

}
