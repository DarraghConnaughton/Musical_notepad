package com.darragh.musicalnotepad.Pagers;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.darragh.musicalnotepad.Modules.JavaScriptInterface;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment{
    private Button stop, play, record, save, discard;
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
    public Spinner spinner1;

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
        Song song = new Song(time,"NEW TEST",currentSong.getNotes(),"4/4",keySignature.getAbcFormat());
        Map<String,Object> map = new HashMap<>();
        map.put("/users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/songId/"+time+"/",song);
        databaseReference.updateChildren(map);
    }

    public Song formatSong(String noteProgression,KeySignature keySignature){
        return new Song(Long.toString(System.currentTimeMillis()),"Recorded Song.",
                noteProgression,"4/4",keySignature.getAbcFormat());
    }

//    public Song songFromJSON(Iterable<DataSnapshot> dataSnapshot){
//        Song song = new Song();
//        for(DataSnapshot snap: dataSnapshot){
//            System.out.println(snap + " - " +snap.getValue());
//            switch(snap.getKey()){
//                case "name" :
//                    song.setName((String)snap.getValue());
//                    break;
//                case "keySignature" :
//                    song.setKeySignature((String)snap.getValue());
//                    break;
//                case "l" :
//                    song.setL((String)snap.getValue());
//                    break;
//                case "notes" :
//                    song.setNotes((String)snap.getValue());
//                    break;
//                case "timeSignature" :
//                    song.setTimeSignature((String)snap.getValue());
//                    break;
//                case "timestamp" :
//                    song.setTimestamp((String)snap.getValue());
//                    break;
//            }
//        }
//        System.out.println("SONG!");
//        song.printDetails();
//        return song;
//    }


    private void addItemsSpinner(){
        spinner1 = (Spinner) RootView.findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();

        //PUT THESE INTO XML FILE!!
        list.add("CMajor");
        list.add("GMajor");
        list.add("DMajor");
        list.add("AMajor");
        list.add("EMajor");
        list.add("BMajor");

        list.add("FMajor");
        list.add("BbMajor");
        list.add("EbMajor");
        list.add("AbMajor");
        list.add("DbMajor");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RootView.getContext(),android.R.layout.simple_spinner_item,list);
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
    }

    public void instantiateButtons(){
        stop = (Button) RootView.findViewById(R.id.stop);
        play = (Button) RootView.findViewById(R.id.play);
        record = (Button) RootView.findViewById(R.id.record);
        discard = (Button) RootView.findViewById(R.id.discard);
        save = (Button) RootView.findViewById(R.id.save);
        myWebView = (WebView) RootView.findViewById(R.id.webview);

        stop.setEnabled(false);
        play.setEnabled(false);
        record.setEnabled(true);
        save.setEnabled(false);
        discard.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        discard.setVisibility(View.INVISIBLE);
    }

    private void setClickListeners(){
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record.setEnabled(false);
                stop.setEnabled(true);
                play.setEnabled(false);
                pitchDetector.recordAudio();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = false;
                stop.setEnabled(false);
                record.setEnabled(true);
                play.setEnabled(true);
                currentSong=WebViewController.enableWebView(formatSong(pitchDetector.stopRecording(outputDisplay,keySignature),keySignature),getActivity().getApplicationContext(),myWebView);
                stop.setEnabled(false);
                record.setEnabled(false);
                play.setEnabled(false);
                save.setEnabled(true);
                discard.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                discard.setVisibility(View.VISIBLE);
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setEnabled(true);
                WebViewController.disableWebView();
                save.setEnabled(false);
                discard.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                discard.setVisibility(View.INVISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSong();
                getActivity().finish();
                startActivity(new Intent(getActivity().getApplicationContext(),PagerControl.class));
            }
        });
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
        outputDisplay = (TextView) RootView.findViewById(R.id.textArea);
        outputDisplay.setVisibility(View.GONE);
        setClickListeners();


//        EditText editText = (EditText) RootView.findViewById(R.id.message);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                tuneName=v.getText().toString();
//                return false;
//            }
//        });
    }
}
