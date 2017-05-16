package com.darragh.musicalnotepad.Pitch_Detector;

class FrequencyController {
    public static Frequency scaleFrequencyDown(Frequency freq_octave){
        if(freq_octave.getFrequency()>538.808f && freq_octave.getFrequency()<=1077.616f){
            float temp_frequency;
            for(float i=2.0f; i<10.0f; i++){
                temp_frequency = freq_octave.getFrequency()/i;
                if(temp_frequency>=261.626f && temp_frequency<=538.808f){
                    freq_octave.setOctave((int)i);
                    freq_octave.setFrequency(freq_octave.getFrequency()/i);
                    break;
                }
            }
        }
        else if(freq_octave.getFrequency()>=1077.616f){
            float temp_frequency;
            for(float i=4.0f; i<10.0f; i++){
                temp_frequency = freq_octave.getFrequency()/i;
                if(temp_frequency>=254.284f && temp_frequency<=538.808f){
                    freq_octave.setOctave((int)(i-1.0f));
                    freq_octave.setFrequency(freq_octave.getFrequency()/i);
                    break;
                }
            }
        }
        return freq_octave;
    }
}
