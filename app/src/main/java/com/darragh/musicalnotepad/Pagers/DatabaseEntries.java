package com.darragh.musicalnotepad.Pagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.darragh.musicalnotepad.*;
import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DatabaseEntries extends AppCompatActivity {
    private ListView listView;
    private static String songId,users,Timestamp;
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;
    private final ArrayList<String> listEntriesName = new ArrayList<>();
    private final ArrayList<String> listEntriesID = new ArrayList<>();
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private String[] navigationOptions;
    private static ArrayAdapter<String> adapter;
    private static ListView drawerList;
    private DrawerLayout drawerLayout;


    private void instantiateVariables(){
        songId=getResources().getString(R.string.songId);
        users=getResources().getString(R.string.users);
        Timestamp=getResources().getString(R.string.Timestamp);
    }

    private void fillListEntries(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(songId).getChildren();
        for(DataSnapshot data: snap){
            listEntriesName.add(data.child(getResources().getString(R.string.name)).getValue().toString());
            listEntriesID.add(data.child(getResources().getString(R.string.timestamp)).getValue().toString());
        }
    }

    private void normalClick(final DataSnapshot dataSnapshot){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songFromJSON(dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(songId).child(listEntriesID.get(position)).getChildren());
                Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                intent.putExtra(Timestamp,dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(songId).child(listEntriesID.get(position)).toString());
                finish();
                startActivity(intent);

            }
        });
    }

    private void generatePopupMenu(View view, final int position, final DataSnapshot dataSnapshot){
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals(getResources().getString(R.string.deleteEntry))){
                    Toast.makeText(getApplicationContext(), "DELETE!", Toast.LENGTH_SHORT).show();
                    databaseReference.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).setValue(null);
                    finish();
                    startActivity(new Intent(getApplicationContext(), DatabaseEntries.class));
                } else if(item.getTitle().equals(getResources().getString(R.string.displayEntry))){
                    Toast.makeText(getApplicationContext(), "Display!", Toast.LENGTH_SHORT).show();
                    Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).getChildren();
                    songFromJSON(snap);
                    Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                    intent.putExtra(Timestamp,dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).toString());
                    finish();
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
                listView.setAdapter(new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,listEntriesName));
                normalClick(dataSnapshot);
                longClick(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void instantiateView(){
        listView = (ListView) findViewById(R.id.list);
        listView.setClickable(true);
        firebaseController();
    }

    private void loadActivity(int position){
        switch(position){
            case 0:
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case 1:
                finish();
                startActivity(new Intent(getApplicationContext(), DatabaseEntries.class));
                break;
            case 3:
                firebaseAuth.getInstance().signOut();
                break;
        }
    }

    private void addDrawerItems() {
        navigationOptions = getResources().getStringArray(R.array.navigationOptions);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navigationOptions);
        drawerList.setAdapter(adapter);
    }

    private void setUpNavBar(){
        activityTitle = getTitle().toString();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navList);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + " - " + view + " - " + parent);
                loadActivity(position);
            }
        });
        addDrawerItems();
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.databaseentries);
        setUpNavBar();
        instantiateView();
        instantiateVariables();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Song songFromJSON(Iterable<DataSnapshot> dataSnapshot){
        Song song = new Song();
        for(DataSnapshot snap: dataSnapshot){
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


