package com.darragh.musicalnotepad.Pitch_Detector;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.darragh.musicalnotepad.Modules.NavigationView_Details;
import com.darragh.musicalnotepad.Objects.Frequency;
import com.darragh.musicalnotepad.R;
import com.firebase.client.Firebase;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Tuner extends AppCompatActivity{
    private static Thread t;
    private Handler handler = new Handler();
    private AudioDispatcher dispatcher;
    private float note,pitch_freq;
    private boolean inTune;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView currentNote,flat,sharp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        startTuner();
        setContentView(R.layout.tuner);
        instantiateView();
        setUpNavigationBar();
    }

    private void setNullView(){
        flat.setText("");
        sharp.setText("");
    }

    private void instantiateView(){
        currentNote = (TextView) findViewById(R.id.currentNote);
        flat = (TextView) findViewById(R.id.flat);
        sharp = (TextView) findViewById(R.id.sharp);
    }

    private void setFlatOrSharp(){
        if(Math.round(pitch_freq)<=Math.round(note) && !currentNote.getText().equals("")){
            sharp.setText(Math.round(note-pitch_freq) + "Hz - \u266D");
            flat.setText("");
        }
        else if(Math.round(pitch_freq)>=Math.round(note) && !currentNote.getText().equals("")){
           flat.setText(Math.round(pitch_freq-note)+ "Hz - \u266F");
            sharp.setText("");
        }
    }

    private void updateDisplay(final float pitch){
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentNote.setText(hz_to_note(pitch));
                if(!inTune){
                    setFlatOrSharp();
                    currentNote.setTypeface(null, Typeface.NORMAL);
                    currentNote.setTextColor(Color.RED);
                } else
                {
                    setNullView();
                    currentNote.setTypeface(null, Typeface.BOLD);
                    currentNote.setTextColor(Color.GREEN);
                }
            }
        });
    }

    public void startTuner(){
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent audioEvent) {
                updateDisplay(result.getPitch());
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        t = new Thread(dispatcher, "Tuner");
        t.start();

    }

    private void checkIntune(float frequency, int lowerBound, int higherBound){
        if(Math.round(frequency)>=lowerBound && Math.round(frequency)<=higherBound){
            inTune=true;
        }
        else{
            inTune=false;
        }
    }

    public String hz_to_note(float frequency){
        Frequency freq_octave = new Frequency(frequency,1);
        if(frequency>538.808f){
            freq_octave = FrequencyController.scaleFrequencyDown(freq_octave);
        }
        pitch_freq=freq_octave.getFrequency();
        if(freq_octave.getFrequency()>=254.284f && freq_octave.getFrequency()<=269.4045f){
            checkIntune(freq_octave.getFrequency(),260,262);
            note=261.63f;
            return "C";
        } else if(freq_octave.getFrequency()>269.4045f && freq_octave.getFrequency()<=285.424f){
            note=277.18f;
            checkIntune(freq_octave.getFrequency(),276,278);
            return "D\u266D";
        } else if(freq_octave.getFrequency()>285.424f && freq_octave.getFrequency()<=302.396f){
            checkIntune(freq_octave.getFrequency(),292,294);
            note=293.66f;
            return "D";
        } else if(freq_octave.getFrequency()>302.396f && freq_octave.getFrequency()<=320.3775f){
            checkIntune(freq_octave.getFrequency(),310,312);
            note=311.13f;
            return "E\u266D";
        } else if(freq_octave.getFrequency()>320.3775f && freq_octave.getFrequency()<=339.428f){
            checkIntune(freq_octave.getFrequency(),329,331);
            note=329.63f;
            return "E";
        } else if(freq_octave.getFrequency()>339.428f && freq_octave.getFrequency()<=359.611f){
            checkIntune(freq_octave.getFrequency(),348,350);
            note=349.23f;
            return "F";
        } else if(freq_octave.getFrequency()>359.611f && freq_octave.getFrequency()<=380.9945f){
            checkIntune(freq_octave.getFrequency(),369,371);
            note=369.99f;
            return "G\u266D";
        } else if(freq_octave.getFrequency()>380.9945f && freq_octave.getFrequency()<=403.65f){
            checkIntune(freq_octave.getFrequency(),391,393);
            note=392.00f;
            return "G";
        } else if(freq_octave.getFrequency()>403.65f && freq_octave.getFrequency()<=427.6525f){
            checkIntune(freq_octave.getFrequency(),414,416);
            note=415.30f;
            return "A\u266D";
        } else if(freq_octave.getFrequency()>427.6525f && freq_octave.getFrequency()<=453.082f){
            checkIntune(freq_octave.getFrequency(),439,441);
            note=440.00f;
            return "A";
        } else if(freq_octave.getFrequency()>453.082f && freq_octave.getFrequency()<=480.0236f){
            checkIntune(freq_octave.getFrequency(),465,467);
            note=466.16f;
            return "B\u266D";
        } else if(freq_octave.getFrequency()>480.0236f && freq_octave.getFrequency()<=508.567f){
            checkIntune(freq_octave.getFrequency(),493,495);
            note=493.88f;
            return "B";
        } else if(freq_octave.getFrequency()>508.567f && freq_octave.getFrequency()<=538.808f){
            checkIntune(freq_octave.getFrequency(),522,524);
            note=523.25f;
            return "C";
        } else
            note=0f;
            inTune=false;
            return "";
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,dispatcher,1);
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
