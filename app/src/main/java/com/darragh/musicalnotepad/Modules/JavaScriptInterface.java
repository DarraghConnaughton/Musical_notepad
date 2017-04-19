package com.darragh.musicalnotepad.Modules;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.darragh.musicalnotepad.Pitch_Detector.Song;

public class JavaScriptInterface {
    private static Context context;
    private static Song song;

    public JavaScriptInterface(Context c){
        context = c;
    }
    public void setSong(Song mSong){
        song=mSong;
    }


    @JavascriptInterface
    public String sendData(){
//        String timeSignature = "4/4";
//        String keySignature = "Emin";
//        String L= "1/8";
//        String noteProgression = "|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
//
//        return "%%staffwidth 200\nX: 1 \nT: " + recordingName + " \nM: " + timeSignature + " \nL: " + L + "\nK: " + keySignature + "\n" + noteProgression;

        System.out.println(song);
        return "%%staffwidth 200\nX: 1 \nT: " + song.getName() + " \nM: " + song.getTimeSignature() + " \nL: " + song.getL() + "\nK: " + song.getKeySignature() + "\n" + song.getNotes();
    }

    @JavascriptInterface
    public String chordProgression(){
        return song.getNotes();
    }

}

