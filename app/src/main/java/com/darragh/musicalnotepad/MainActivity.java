package com.darragh.musicalnotepad;

import android.content.Intent;
import android.media.AudioRecord;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.Login_Register.User;
import com.darragh.musicalnotepad.Modules.JavaScriptInterface;
import com.darragh.musicalnotepad.Pitch_Detector.PitchDetector;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TableLayout tableLayout;
    private ViewPager viewPager;

    private static Button stop, play, record;
    private static TextView outputDisplay;
    private static AudioRecord audioRecorder = null;
    private static File audioFile;
    private static PitchDetector pitchDetector;
    public boolean isRecording;
    public static WebView myWebView;
    public static String tuneName="";
    public static Firebase myFirebaseRef;
    public static FirebaseAuth firebaseAuth;
    public Thread t;
    public AudioDispatcher dispatcher;
    public ArrayList<String> list = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void disableWebView(){
        myWebView.loadUrl("about:blank");
    }
    public void enableWebView(){
        myWebView = (WebView) findViewById(R.id.webview);
        final JavaScriptInterface jsInterface = new JavaScriptInterface(this);
//        jsInterface.setRecordingName(tuneName);

        myWebView.addJavascriptInterface(jsInterface, "Android");
        myWebView.loadUrl("file:///android_asset/webDisplay.html");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    protected void instantiateButtons() {
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        pitchDetector = new PitchDetector();
        outputDisplay = (TextView) findViewById(R.id.textView);
        outputDisplay.setVisibility(View.GONE);
        stop.setEnabled(false);
        play.setEnabled(false);
        record.setEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,SignIn.class));
        }

//        EditText editText = (EditText) findViewById(R.id.message);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                tuneName=v.getText().toString();
//                return false;
//            }
//        });
    }

    private void logout(){
        finish();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, SignIn.class));
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        myFirebaseRef.child("users").child(userId).setValue(user);
    }

    public static void writeToFirebase(String name, String meter, String noteProgression){

    }

    @Override
    public void onClick(View view){
        if(view == stop){
            isRecording = false;
            stop.setEnabled(false);
            record.setEnabled(true);
            play.setEnabled(true);
            disableWebView();
            logout();
//            pitchDetector.stopRecording(outputDisplay,);
        }
        if(view == record){
            record.setEnabled(false);
            stop.setEnabled(true);
            play.setEnabled(false);
            enableWebView();
            pitchDetector.recordAudio();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        instantiateButtons();

        Firebase myFirebaseRef = new Firebase("https://musicalnotepad-9002a.firebaseio.com/");
        stop.setOnClickListener(this);
        record.setOnClickListener(this);
        play.setOnClickListener(this);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record.setEnabled(false);
                stop.setEnabled(true);
                play.setEnabled(false);
                enableWebView();
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
                disableWebView();
                logout();
//                pitchDetector.stopRecording(outputDisplay);
            }
        });

//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("PLAY BUTTON ACTIVATED");
////                playAudio();
//            }
//        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
