package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.darragh.musicalnotepad.R;
import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment2 extends Fragment {
    public View RootView;
    private ListView listView;
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;
    private final ArrayList<String> listEntriesName = new ArrayList<>();
    private final ArrayList<String> listEntriesID = new ArrayList<>();

    private void fillListEntries(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> snap = dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("/songId/").getChildren();
        for(DataSnapshot data: snap){
            listEntriesName.add(data.child("name").getValue().toString());
            listEntriesID.add(data.child("timestamp").getValue().toString());
        }
    }

    private void normalClick(final DataSnapshot dataSnapshot){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songFromJSON(dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("/songId/").child(listEntriesID.get(position)).getChildren());
                Intent intent = new Intent(getActivity().getApplicationContext(),songDisplay.class);
                intent.putExtra("Timestamp",dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("/songId/").child(listEntriesID.get(position)).toString());
                getActivity().finish();
                startActivity(intent);

            }
        });
    }

    private void generatePopupMenu(View view, final int position, final DataSnapshot dataSnapshot){
        PopupMenu popupMenu = new PopupMenu(getContext().getApplicationContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Delete Entry")){
                    Toast.makeText(getContext().getApplicationContext(), "DELETE!", Toast.LENGTH_SHORT).show();
                    databaseReference.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("/songId/").child(listEntriesID.get(position)).setValue(null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity().getApplicationContext(), PagerControl.class));
                } else if(item.getTitle().equals("Display Entry")){
                    Toast.makeText(getContext().getApplicationContext(), "Display!", Toast.LENGTH_SHORT).show();
                    Iterable<DataSnapshot> snap = dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("/songId/").child(listEntriesID.get(position)).getChildren();
                    songFromJSON(snap);
                    Intent intent = new Intent(getActivity().getApplicationContext(),songDisplay.class);
                    intent.putExtra("Timestamp",dataSnapshot.child("/users/").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("/songId/").child(listEntriesID.get(position)).toString());
                    getActivity().finish();
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void longClick(final DataSnapshot dataSnapshot){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                generatePopupMenu(view,position,dataSnapshot);
                return true;
            }
        });
    }

    private void firebaseController(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                fillListEntries(dataSnapshot);
                listView.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,listEntriesName));
                normalClick(dataSnapshot);
                longClick(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void instantiateView(){
        listView = (ListView) RootView.findViewById(R.id.list);
        listView.setClickable(true);
        firebaseController();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        RootView = inflater.inflate(R.layout.databaseentries,container,false);
        instantiateView();
        firebaseAuth = FirebaseAuth.getInstance();
        return RootView;
    }

    public Song songFromJSON(Iterable<DataSnapshot> dataSnapshot){
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
            }
        }
        song.printDetails();
        return song;
    }
}


