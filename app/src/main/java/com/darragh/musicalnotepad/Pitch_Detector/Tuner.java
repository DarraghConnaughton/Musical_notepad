package com.darragh.musicalnotepad.Pitch_Detector;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.darragh.musicalnotepad.Pagers.NavigationView_Details;
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
    private Thread t;
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
        setContentView(R.layout.tuner);
        startTuner();
        setUpNavigationBar();
        instantiateView();

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
            flat.setText(null);
        }
        else if(Math.round(pitch_freq)>=Math.round(note) && !currentNote.getText().equals("")){
           flat.setText(Math.round(pitch_freq-note)+ "Hz - \u266F");
            sharp.setText(null);
        }
    }

    private void updateDisplay(final float pitch){
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentNote.setText(hz_to_note(pitch));
                if(!inTune){
                    setFlatOrSharp();
                    currentNote.setTextColor(Color.RED);
                } else
                {
                    setNullView();
                    currentNote.setTextColor(Color.GREEN);
                }
            }
        });
    }

    /// PRINT FROM WEBVIEW

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
        t = new Thread(dispatcher, "Audio Dispatcher");
        t.start();
    }

    public String hz_to_note(float frequency){
        if(frequency>538.808f && frequency<=1077.616f){
            float temp_frequency;
            for(float i=2.0f; i<10.0f; i++){
                temp_frequency = frequency/i;
                if(temp_frequency>=261.626f && temp_frequency<=538.808f){
                    frequency = frequency/i;
                    break;
                }
            }
        }
        else if(frequency>=1077.616f){
            float temp_frequency;
            for(float i=4.0f; i<10.0f; i++){
                temp_frequency = frequency/i;
                if(temp_frequency>=254.284f && temp_frequency<=538.808f){
                    frequency = frequency/i;
                    break;
                }
            }
        }
        pitch_freq=frequency;
        if(frequency>=254.284f && frequency<=269.4045f){
            if(Math.round(frequency)>=260 && Math.round(frequency)<=262){
                inTune=true;
            }
            else{
                inTune=false;
            }
            note=261.63f;
            return "C";
        } else if(frequency>269.4045f && frequency<=285.424f){
            note=277.18f;
            if(Math.round(frequency)>=276 && Math.round(frequency)<=278){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "D\u266D";
        } else if(frequency>285.424f && frequency<=302.396f){
            if(Math.round(frequency)>=292 && Math.round(frequency)<=294){
                inTune=true;
            }
            else{
                inTune=false;
            }
            note=293.66f;
            return "D";
        } else if(frequency>302.396f && frequency<=320.3775f){
            note=311.13f;
            if(Math.round(frequency)>=310 && Math.round(frequency)<=312){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "E\u266D";
        } else if(frequency>320.3775f && frequency<=339.428f){
            if(Math.round(frequency)>=329 && Math.round(frequency)<=331){
                inTune=true;
            }
            else{
                inTune=false;
            }
            note=329.63f;
            return "E";
        } else if(frequency>339.428f && frequency<=359.611f){
            note=349.23f;
            if(Math.round(frequency)>=348 && Math.round(frequency)<=350){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "F";
        } else if(frequency>359.611f && frequency<=380.9945f){
            note=369.99f;
            if(Math.round(frequency)>=369 && Math.round(frequency)<=371){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "G\u266D";
        } else if(frequency>380.9945f && frequency<=403.65f){
            note=392.00f;
            if(Math.round(frequency)>=391 && Math.round(frequency)<=393){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "G";
        } else if(frequency>403.65f && frequency<=427.6525f){
            note=415.30f;
            if(Math.round(frequency)>=414 && Math.round(frequency)<=416){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "A\u266D";
        } else if(frequency>427.6525f && frequency<=453.082f){
            note=440.00f;
            if(Math.round(frequency)>=439 && Math.round(frequency)<=441){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "A";
        } else if(frequency>453.082f && frequency<=480.0236f){
            note=466.16f;
            if(Math.round(frequency)>=465 && Math.round(frequency)<=467){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "B\u266D";
        } else if(frequency>480.0236f && frequency<=508.567f){
            note=493.88f;
            if(Math.round(frequency)>=493 && Math.round(frequency)<=495){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "B";
        } else if(frequency>508.567f && frequency<=538.808f){
            note=523.25f;
            if(Math.round(frequency)>=522 && Math.round(frequency)<=524){
                inTune=true;
            }
            else{
                inTune=false;
            }
            return "C";
        } else
            note=0f;
            return "";
    }

    private void setUpNavigationBar(){
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationBar = (NavigationView) findViewById(R.id.navigationBar);
        NavigationView_Details.setNavigationView(navigationBar,getApplicationContext(),this,mDrawerLayout,dispatcher);
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
