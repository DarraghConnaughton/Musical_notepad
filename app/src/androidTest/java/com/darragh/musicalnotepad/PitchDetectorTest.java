package com.darragh.musicalnotepad;

import com.darragh.musicalnotepad.Pagers.Fragment1;
import com.darragh.musicalnotepad.Pitch_Detector.PitchDetector;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by darragh on 29/03/17.
 */
public class PitchDetectorTest {
    private static float[] testNotes = {
            254.285f,269.4045f,    //"C: testing frequency boundaries."
            269.4056f,285.424f,    //"D\u266D: testing frequency boundaries."
            285.425f,302.396f,     //"D: testing frequency boundaries."
            302.398f,320.3775f,    //"E\u266D: testing frequency boundaries."
            320.3776f,339.428f,    //"E: testing frequency boundaries."
            339.43f,359.611f,      //"F: testing frequency boundaries."
            359.612f,380.9945f,    //"G:\u266D: testing frequency boundaries."
            380.9946f,403.65f,     //"G: testing frequency boundaries."
            403.66f,427.6525f,     //"A\u266D: testing frequency boundaries."
            427.6526f,453.082f,    //"A: testing frequency boundaries."
            453.083f,480.0236f,    //"B\u226D: testing frequency boundaries."
            480.0237f,508.567f,    //"B: testing frequency boundaries."
            508.568f,538.808f      //"C: testing frequency boundaries."
    };
    private static String[] expectedOutput = {
            "C","C",
            "D♭","D♭",
            "D","D",
            "E♭","E♭",
            "E","E",
            "F","F",
            "G♭","G♭",
            "G","G",
            "A♭","A♭",
            "A","A",
            "B♭","B♭",
            "B","B",
            "C","C",
    };
//    @Test
//    public void recordAudio() throws Exception {
//
//    }
//
//    @Test
//    public void stopRecording() throws Exception {
//
//    }
    public static int[] expectedOutput(){
        int[] expected = {8,6,4,3,6,9};
        return expected;
    }

    @Test
    public void getBeats(){
        String[] testInput = {"4/4","3/4","2/4","3/8","6/8","9/8"};
        int[] expected = expectedOutput();
        for(int i=0; i<expected.length; i++){
            assertEquals(expected[i], Fragment1.getBeats(testInput[i]));
        }
    }

    public String[] assignOctave(String[] notes, int octave){
        String[] notes_with_octave = new String[notes.length];
        for(int i=0; i<notes.length; i++){
            if(i<notes.length-2){
                notes_with_octave[i] = notes[i] + octave;
            } else {
                notes_with_octave[i] = notes[i] + (octave+1);
            }
        }
        return notes_with_octave;
    }

    public String[] returnPitches(int octave){
        String[] actualOutput = new String[expectedOutput.length];
        PitchDetector pitchDetector = new PitchDetector();
        for(int i=0; i<testNotes.length; i++){
            actualOutput[i] = pitchDetector.hz_to_note(testNotes[i]*octave,1);
            System.out.println(actualOutput[i]);
        }
        return actualOutput;
    }

    @Test
    public void hz_to_note_octave1() throws Exception {
        assertArrayEquals(assignOctave(expectedOutput,1),returnPitches(1));
    }

    @Test
    public void hz_to_note_octave2() throws Exception {
        assertArrayEquals(assignOctave(expectedOutput,2),returnPitches(2));
    }
    @Test
    public void hz_to_note_octave3() throws Exception {
        assertArrayEquals(assignOctave(expectedOutput,3),returnPitches(4));
    }

}