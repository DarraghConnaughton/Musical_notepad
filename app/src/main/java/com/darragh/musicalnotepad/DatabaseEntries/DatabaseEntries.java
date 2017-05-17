package com.darragh.musicalnotepad.DatabaseEntries;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.darragh.musicalnotepad.*;
import com.darragh.musicalnotepad.Modules.DialogController;
import com.darragh.musicalnotepad.Modules.JSONToSongConverter;
import com.darragh.musicalnotepad.Modules.NavigationView_Details;
import com.darragh.musicalnotepad.Modules.songDisplay;
import com.darragh.musicalnotepad.Objects.SongArrayObject;
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
    private static String songId,users,Timestamp,profilePhoto;
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;
    private final ArrayList<String> listEntriesName = new ArrayList<>();
    private final ArrayList<String> listEntriesID = new ArrayList<>();
    private final ArrayList<String> listEntriesTimesignature = new ArrayList<>();
    private final ArrayList<String> listEntriesKeysignature = new ArrayList<>();
    private final ArrayList<String> listEntriesTimestamp = new ArrayList<>();
    private final ArrayList<String> listEntriesProfilePhoto = new ArrayList<>();
    private NavigationView navigationBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private void instantiateVariables(){
        songId=getResources().getString(R.string.songId);
        users=getResources().getString(R.string.users);
        Timestamp=getResources().getString(R.string.Timestamp);
    }

    private void fillListEntries(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(songId).getChildren();
        for(DataSnapshot data: snap){
            listEntriesName.add(data.child(getResources().getString(R.string.name)).getValue().toString());
            listEntriesID.add(data.child(getResources().getString(R.string.timestamp)).getValue().toString());
            listEntriesKeysignature.add(data.child("/keySignature/").getValue().toString());
            listEntriesTimesignature.add(data.child("/timeSignature/").getValue().toString());
            listEntriesTimestamp.add(data.getKey());
            if(data.child("/profilePhoto/").exists()){
                listEntriesProfilePhoto.add(data.child("/profilePhoto/").getValue().toString());
            }
            else {
                listEntriesProfilePhoto.add("1");
            }
        }
    }

    private void normalClick(final DataSnapshot dataSnapshot){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONToSongConverter.songFromJSON(dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(songId).child(listEntriesID.get(position)).getChildren());
                Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                intent.putExtra(Timestamp,listEntriesID.get(position));
                intent.putExtra("Directory","/songId/");
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
                }
                else if(item.getTitle().equals(getResources().getString(R.string.displayEntry))){
                    Toast.makeText(getApplicationContext(), "Display!", Toast.LENGTH_SHORT).show();
                    Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).getChildren();
                    JSONToSongConverter.songFromJSON(snap);
                    Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                    intent.putExtra(Timestamp,listEntriesID.get(position));
                    intent.putExtra("Directory",users);
                    finish();
                    startActivity(intent);
                }
                else if(item.getTitle().equals(getResources().getString(R.string.shareEntry))) {
                    Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).getChildren();
                    DialogController.getFriendsList(snap, DatabaseEntries.this, users);
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
                SongArrayObject songArray = new SongArrayObject(listEntriesName, listEntriesID,listEntriesTimesignature,
                        listEntriesKeysignature,listEntriesTimestamp);
                songArray.setProfilePhoto(listEntriesProfilePhoto);
                SongListAdapter adapter2 = new SongListAdapter(getApplicationContext(),songArray);
                listView.setAdapter(adapter2);

                normalClick(dataSnapshot);
                longClick(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void instantiateView(){
        listView = (ListView) findViewById(R.id.list);
        firebaseController();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.databaseentries);
        instantiateView();
        instantiateVariables();
        setUpNavigationBar();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,null,0);
        setActionBarDetails(mDrawerLayout);
    }

    private void setActionBarDetails(DrawerLayout mDrawerLayout){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF18003E")));
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item)||actionBarDrawerToggle.onOptionsItemSelected(item);
    }
}


