package com.darragh.musicalnotepad.Pagers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
    private static String songId,users,Timestamp,profilePhoto;
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference databaseReference;
    private final ArrayList<String> listEntriesName = new ArrayList<>();
    private final ArrayList<String> listEntriesID = new ArrayList<>();
    private final ArrayList<String> listEntriesTimesignature = new ArrayList<>();
    private final ArrayList<String> listEntriesKeysignature = new ArrayList<>();
    private final ArrayList<String> listEntriesTimestamp = new ArrayList<>();
    private final ArrayList<String> listEntriesProfilePhoto = new ArrayList<>();
    private String activityTitle;
    private String[] navigationOptions, emailArray;
    private static ArrayAdapter<String> adapter;
    private static ListView drawerList;

    private DrawerLayout drawerLayout;
    private ArrayList<UserProfileDetails> listDetails;


    private void instantiateVariables(){
        songId=getResources().getString(R.string.songId);
        users=getResources().getString(R.string.users);
        Timestamp=getResources().getString(R.string.Timestamp);
    }

    private void fillListEntries(DataSnapshot dataSnapshot){
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(songId).getChildren();
        for(DataSnapshot data: snap){
            System.out.println("Snap: " + snap);
            System.out.println(data.child(getResources().getString(R.string.name)).getValue().toString());
            System.out.println(data.child(getResources().getString(R.string.name)).getValue().toString());
            System.out.println(data.child(data.child(getResources().getString(R.string.timestamp)).getValue().toString()));
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
                songFromJSON(dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(songId).child(listEntriesID.get(position)).getChildren());
                Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                intent.putExtra(Timestamp,listEntriesID.get(position));
                intent.putExtra("Directory",users);
                finish();
                startActivity(intent);

            }
        });
    }

//    private void createFriendSelectionDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.dialog_title);
//
//        builder.setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//                System.out.println(listEntriesID.get(id));
//            }
//        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });
//        AlertDialog dialog = builder.create();
//    }

    private String[] getEmailAddresses(ArrayList<UserProfileDetails> listDetails){
        System.out.println(listDetails.size());
        String[] emails = new String[listDetails.size()];
        for(int i=0; i<listDetails.size(); i++){
            emails[i] = listDetails.get(i).emailAddress;
        }
        return emails;
    }

    private void setProfilePhoto(DataSnapshot data){
        System.out.println("SetProfilePhoto");

        System.out.println(data.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists());
        if(data.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
            profilePhoto = data.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").getValue().toString();
        }
        System.out.println("Profile Photo: " + profilePhoto);
    }

    public ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot){
        setProfilePhoto(dataSnapshot);
        System.out.println(dataSnapshot);
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            System.out.println(dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()));
            System.out.println("Key: "+ data.getKey());
            usersFound.add(new UserProfileDetails(
                    dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("username").getValue().toString(),
                    dataSnapshot.child(getResources().getString(R.string.users)).child(data.getKey()).child("email").getValue().toString(),
                    data.getKey()));
        }
        return usersFound;
    }

    private ArrayList<String> selectedFriends(ArrayList<Integer> mSelectedItems,ArrayList<UserProfileDetails> listDetails){
        ArrayList<String> selectedFriends = new ArrayList<>();

        for(int x: mSelectedItems){
            selectedFriends.add(listDetails.get(x).UID);
        }
        return selectedFriends;

    }

    private void getFriendsList(final Iterable<DataSnapshot> snap){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                onCreateDialog(gatherUsers(dataSnapshot),snap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    public void onCreateDialog(final ArrayList<UserProfileDetails> listDetails, final Iterable<DataSnapshot> snap) {
        // Where we track the selected items
        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseEntries.this);
        String[] entries = getEmailAddresses(listDetails);
        for(String e: entries){
            System.out.println(e);
        }
        // Set the dialog title
        builder.setTitle(R.string.dialog_title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(entries, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        SongSharer.shareSong(songFromJSON(snap),selectedFriends(mSelectedItems,listDetails),profilePhoto);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.create();
        builder.show();
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
                    songFromJSON(snap);
                    Intent intent = new Intent(getApplicationContext(),songDisplay.class);
                    intent.putExtra(Timestamp,listEntriesID.get(position));
                    intent.putExtra("Directory",users);
                    finish();
                    startActivity(intent);
                }
                else if(item.getTitle().equals(getResources().getString(R.string.shareEntry))) {
                    Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(songId).child(listEntriesID.get(position)).getChildren();
                    System.out.println("OnCreate Dialog:");
                    getFriendsList(snap);
                }
                System.out.println("SELECTED FRIENDS");

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navigationOptions);
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
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
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
        return song;
    }
}


