package com.darragh.musicalnotepad.Pagers;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment{
    private Button stop, record, save, discard;
    private TextView outputDisplay;
    private static PitchDetector pitchDetector;
    public boolean isRecording;
    public WebView myWebView;
    public static String tuneName="";
    public static FirebaseAuth firebaseAuth;
    public View RootView;
    public static Song currentSong;
    public static DatabaseReference databaseReference;
    public Thread t;
    private static KeySignature keySignature;
    private static EditText enterSongName;
    private static ListView drawerList;
    private static String timeSignature;
    private DrawerLayout drawerLayout;
    public Spinner spinner1,spinner2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        RootView = inflater.inflate(R.layout.activity_main,container,false);
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        instantiateView();
        return RootView;
    }

    public void saveSong(){
        String time = Long.toString(System.currentTimeMillis());
        Song song = new Song(time,tuneName,currentSong.getNotes(),timeSignature,keySignature.getAbcFormat());
        Map<String,Object> map = new HashMap<>();
        map.put(getActivity().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                .getUid()+getActivity().getResources().getString(R.string.songId)+time+"/",song);
        databaseReference.updateChildren(map);
    }

    public Song formatSong(String noteProgression,KeySignature keySignature){
        return new Song(Long.toString(System.currentTimeMillis()),"",
                noteProgression,timeSignature,keySignature.getAbcFormat());
    }

    private void addItemsSpinner(){
        spinner1 = (Spinner) RootView.findViewById(R.id.spinner1);
        List<String> keysignatureList = Arrays.asList(getActivity().getResources().getStringArray(R.array.keysignatureArray));


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RootView.getContext(),android.R.layout.simple_spinner_item,keysignatureList);
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

        spinner2 = (Spinner) RootView.findViewById(R.id.spinner2);
        List<String> list2 = Arrays.asList(getActivity().getResources().getStringArray(R.array.timesignatureArray));

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(RootView.getContext(),android.R.layout.simple_spinner_item,list2);
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
        stop = (Button) RootView.findViewById(R.id.stop);
        record = (Button) RootView.findViewById(R.id.record);
        discard = (Button) RootView.findViewById(R.id.discard);
        save = (Button) RootView.findViewById(R.id.save);
        myWebView = (WebView) RootView.findViewById(R.id.webview);
        enterSongName = (EditText) RootView.findViewById(R.id.enterSongName);
        outputDisplay = (TextView) RootView.findViewById(R.id.textView);

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
                currentSong=WebViewController.enableWebView(formatSong(pitchDetector.stopRecording(outputDisplay,keySignature,getBeats(timeSignature)),keySignature),getActivity().getApplicationContext(),myWebView);
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
                saveSong();
                getActivity().finish();
                startActivity(new Intent(getActivity().getApplicationContext(),PagerControl.class));
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
            getActivity().finish();
            startActivity(new Intent(getActivity().getApplicationContext(),SignIn.class));
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
