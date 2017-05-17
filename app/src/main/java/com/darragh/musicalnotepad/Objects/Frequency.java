package com.darragh.musicalnotepad.Objects;

public class Frequency {
    private static float frequency;
    private static int octave;

    public Frequency(float _frequency, int _octave){
        frequency=_frequency;
        octave=_octave;
    }
    public float getFrequency(){
        return frequency;
    }
    public int getOctave(){
        return octave;
    }
    public static void setFrequency(float _frequency){
        frequency=_frequency;
    }

    public static void setOctave(int _octave) {
        octave = _octave;
    }
}
