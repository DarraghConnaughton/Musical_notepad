package com.darragh.musicalnotepad.Pagers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by darragh on 27/04/17.
 */

public class songRequestList extends AppCompatActivity {
    private String users;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setStrings();
        setContentView(R.layout.songrequestlist);
        instantiateView();
    }

    private void setStrings(){
        users=getResources().getString(R.string.users);
    }

    private void instantiateView(){
        final ListView listView = (ListView) findViewById(R.id.listView);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ArrayList<Song> songDetails = gatherSongDetails(dataSnapshot);
                SongRequestListAdapter potentialSongList = new SongRequestListAdapter(getApplicationContext(),songDetails);
                listView.setAdapter(potentialSongList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private ArrayList<Song> gatherSongDetails(DataSnapshot dataSnapshot){
        ArrayList<Song> gatheredSongs = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/SongRequest/").getChildren();
        for(DataSnapshot data: snap){
            Song newSong = new Song(
                    Long.toString(System.currentTimeMillis()),
                    data.child("name").getValue().toString(),
                    data.child("notes").getValue().toString(),
                    data.child("timeSignature").getValue().toString(),
                    data.child("keySignature").getValue().toString());
            Song.sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(dataSnapshot.child(users+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
                Song.profilePhoto = dataSnapshot.child(users+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").getValue().toString();
            }
            gatheredSongs.add(newSong);
        }
        return gatheredSongs;
    }
}
