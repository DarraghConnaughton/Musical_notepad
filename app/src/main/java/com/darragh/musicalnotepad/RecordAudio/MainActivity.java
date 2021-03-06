package com.darragh.musicalnotepad.RecordAudio;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.darragh.musicalnotepad.Modules.NavigationView_Details;
import com.darragh.musicalnotepad.Modules.WebViewController;
import com.darragh.musicalnotepad.Objects.KeySignature;
import com.darragh.musicalnotepad.Pitch_Detector.PitchDetector;
import com.darragh.musicalnotepad.Pitch_Detector.Tuner;
import com.darragh.musicalnotepad.R;
import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.Objects.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private Button record, save, discard;
    private TextView outputDisplay;
    private static PitchDetector pitchDetector;
    public int isRecording=0;
    public WebView myWebView;
    public static String tuneName="";
    public static FirebaseAuth firebaseAuth;
    public static Song currentSong;
    public static DatabaseReference databaseReference;
    public Thread t;
    private static KeySignature keySignature;
    private static EditText enterSongName;
    private static String timeSignature;
    public Spinner spinner1,spinner2;
    private NavigationView navigationBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            instantiateView();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void saveSong(){
        String time = Long.toString(System.currentTimeMillis());
        System.out.println(time + " - " + tuneName);
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
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Arrays.asList(getResources().getStringArray(R.array.keysignatureArray)));
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
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Arrays.asList(getResources().getStringArray(R.array.timesignatureArray)));
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

    public void instantiateButtons(){
        record = (Button) findViewById(R.id.record);
        record = (Button) findViewById(R.id.record);
        discard = (Button) findViewById(R.id.discard);
        save = (Button) findViewById(R.id.save);
        myWebView = (WebView) findViewById(R.id.webview);
        enterSongName = (EditText) findViewById(R.id.enterSongName);
        outputDisplay = (TextView) findViewById(R.id.textView);

        setEditTextListener();
        record.setEnabled(true);
        save.setEnabled(false);
        discard.setEnabled(false);
        outputDisplay.setVisibility(View.INVISIBLE);
        myWebView.setVisibility(View.INVISIBLE);
        enterSongName.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        discard.setVisibility(View.INVISIBLE);
    }

    private void flashingIcon(final int counter){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(counter%2==1){
                    record.setBackgroundResource(R.drawable.record_icon_clicked);
                    if(isRecording%2==0){
                        return;
                    }
                    else {
                        flashingIcon(counter+1);
                    }
                }
                else {
                    record.setBackgroundResource(R.drawable.record_icon_not_clicked);
                    if(isRecording%2==0){
                        return;
                    }
                    else {
                        flashingIcon(counter+1);
                    }
                }
            }
        }, 400);
    }

    private void stopRecording(){
        record.setEnabled(true);
        record.setBackgroundResource(R.drawable.record_icon_not_clicked);
        currentSong=WebViewController.enableWebView(formatSong(pitchDetector.stopRecording(outputDisplay,keySignature,getBeats(timeSignature)),keySignature),getApplicationContext(),myWebView);
        record.setEnabled(false);
        save.setEnabled(true);
        discard.setEnabled(true);
        TextView enterKey = (TextView) findViewById(R.id.enterKey);
        TextView enterTime = (TextView) findViewById(R.id.enterTime);
        enterKey.setVisibility(View.INVISIBLE);
        enterTime.setVisibility(View.INVISIBLE);
        spinner1.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);
        myWebView.setVisibility(View.VISIBLE);
        enterSongName.setVisibility(View.VISIBLE);
        record.setVisibility(View.INVISIBLE);
        save.setVisibility(View.VISIBLE);
        discard.setVisibility(View.VISIBLE);
    }

    private void setClickListeners(){
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording++;
                NavigationView_Details.incrementRecordCounter();
                if(isRecording%2==1){
                    record.setBackgroundResource(R.drawable.record_icon_clicked);
                    pitchDetector.recordAudio();
                    flashingIcon(0);
                } else
                {
                    stopRecording();
                }
            }

        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setBackgroundResource(R.drawable.record_icon_not_clicked);
                finish();
                startActivity(new Intent(getApplicationContext(),Tuner.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(namedSong()){
                record.setBackgroundResource(R.drawable.record_icon_not_clicked);
                saveSong();
                finish();
                startActivity(new Intent(getApplicationContext(), Tuner.class));
            }
            }
        });
    }

    private static boolean namedSong(){
        return enterSongName.getText().length()>0;
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

    protected void instantiateView() throws InterruptedException{
        instantiateFirebase();
        instantiateButtons();
        addItemsSpinner();
        pitchDetector = new PitchDetector();
        setUpNavigationBar();
        setClickListeners();
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,pitchDetector.getDispatcher(),0);
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

    @Override
    public void onBackPressed() {
    }
}
