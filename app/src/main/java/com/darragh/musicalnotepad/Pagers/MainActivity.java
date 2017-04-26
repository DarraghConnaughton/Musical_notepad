package com.darragh.musicalnotepad.Pagers;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darragh.musicalnotepad.Modules.WebViewController;
import com.darragh.musicalnotepad.Pitch_Detector.KeySignature;
import com.darragh.musicalnotepad.Pitch_Detector.PitchDetector;
import com.darragh.musicalnotepad.R;
import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private Button stop, record, save, discard;
    private TextView outputDisplay;
    private static PitchDetector pitchDetector;
    public boolean isRecording;
    public WebView myWebView;
    public static String tuneName="";
    public static FirebaseAuth firebaseAuth;
    public static Song currentSong;
    public static DatabaseReference databaseReference;
    public Thread t;
    private static KeySignature keySignature;
    private static EditText enterSongName;
    private static ListView drawerList;
    private static ArrayAdapter<String> adapter;
    private static String timeSignature;
    private DrawerLayout drawerLayout;
    public Spinner spinner1,spinner2;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private String[] navigationOptions;



    private void addDrawerItems() {
        navigationOptions = getResources().getStringArray(R.array.navigationOptions);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navigationOptions);
        drawerList.setAdapter(adapter);
    }

    private void setupDrawer(){
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
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
            case 2:
                finish();
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                break;
            case 3:
                finish();
                startActivity(new Intent(getApplicationContext(), FindFriend.class));
                break;
            case 4:
                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        System.out.println("onCreate: START");
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        setUpNavBar();

        Firebase.setAndroidContext(getApplicationContext());
        instantiateView();
        System.out.println("onCreate: END");

    }

    public void saveSong(){
        String time = Long.toString(System.currentTimeMillis());
        Song song = new Song(time,tuneName,currentSong.getNotes(),timeSignature,keySignature.getAbcFormat());
        Map<String,Object> map = new HashMap<>();
        map.put(getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                .getUid()+getResources().getString(R.string.songId)+time+"/",song);
        databaseReference.updateChildren(map);
    }

    public Song formatSong(String noteProgression,KeySignature keySignature){
        return new Song(Long.toString(System.currentTimeMillis()),"",
                noteProgression,timeSignature,keySignature.getAbcFormat());
    }

    private void addItemsSpinner(){
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        List<String> keysignatureList = Arrays.asList(getResources().getStringArray(R.array.keysignatureArray));


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,keysignatureList);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                keySignature = new KeySignature(spinner1.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                keySignature = new KeySignature(spinner1.getSelectedItem().toString());
            }
        });

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list2 = Arrays.asList(getResources().getStringArray(R.array.timesignatureArray));

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSignature=spinner2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timeSignature=spinner2.getSelectedItem().toString();
            }
        });
    }

    private static void setEditTextListener(){
        enterSongName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                tuneName=v.getText().toString();
                return false;
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        }
        );
    }

    public void instantiateButtons(){
        System.out.println("instantiateButtons: START");
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        discard = (Button) findViewById(R.id.discard);
        save = (Button) findViewById(R.id.save);
        myWebView = (WebView) findViewById(R.id.webview);
        enterSongName = (EditText) findViewById(R.id.enterSongName);
        outputDisplay = (TextView) findViewById(R.id.textView);

        setEditTextListener();
        stop.setEnabled(false);
        record.setEnabled(true);
        save.setEnabled(false);
        discard.setEnabled(false);
        outputDisplay.setVisibility(View.INVISIBLE);
        myWebView.setVisibility(View.INVISIBLE);
        enterSongName.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        discard.setVisibility(View.INVISIBLE);
        System.out.println("instantiateButtons: END");
    }

    private void setClickListeners(){
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record.setEnabled(false);
                stop.setEnabled(true);
                pitchDetector.recordAudio();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = false;
                stop.setEnabled(false);
                record.setEnabled(true);
                currentSong=WebViewController.enableWebView(formatSong(pitchDetector.stopRecording(outputDisplay,keySignature,getBeats(timeSignature)),keySignature),getApplicationContext(),myWebView);
                stop.setEnabled(false);
                record.setEnabled(false);
                save.setEnabled(true);
                discard.setEnabled(true);
                myWebView.setVisibility(View.VISIBLE);
                enterSongName.setVisibility(View.VISIBLE);
                record.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                discard.setVisibility(View.VISIBLE);

            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setEnabled(true);
                WebViewController.disableWebView(myWebView);
                save.setEnabled(false);
                discard.setEnabled(false);
                myWebView.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);
                discard.setVisibility(View.INVISIBLE);
                record.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(namedSong()){
                System.out.println("INSIDE NAMED SONG");
                saveSong();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            }
        });
    }

    private static boolean namedSong(){
        System.out.println(tuneName.length());
        return tuneName.length()>0;
    }

    public static int getBeats(String timeSignature){
        if(timeSignature.substring(timeSignature.length()-1,timeSignature.length()).equals("8")){
            return Integer.parseInt(timeSignature.substring(0,1));
        }
        else if(timeSignature.substring(timeSignature.length()-1,timeSignature.length()).equals("4")) {
            return Integer.parseInt(timeSignature.substring(0,1))*2;
        }
        return 0;
    }

    private void instantiateFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
    }

    protected void instantiateView() {
        instantiateFirebase();
        instantiateButtons();
        addItemsSpinner();
        pitchDetector = new PitchDetector();
        setClickListeners();
    }
}
