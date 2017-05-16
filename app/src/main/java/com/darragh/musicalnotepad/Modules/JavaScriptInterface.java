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
        return "%%staffwidth 200\nX: 1 \nT: " + song.getName() + " \nM: " + song.getTimeSignature() + " \nL: " + song.getL() + "\nK: " + song.getKeySignature() + "\n" + song.getNotes();
    }

    @JavascriptInterface
    public String chordProgression(){
        System.out.println(MIDIController.convertABC(song.getKeySignature(),song.getNotes()));
        return song.getNotes();
    }

}

