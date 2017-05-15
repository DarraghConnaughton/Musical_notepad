package com.darragh.musicalnotepad.Pitch_Detector;

/**
 * Created by darragh on 15/05/17.
 */

public class FrequencyController {
    public static float scaleFrequencyDown(float frequency,int octave){
        if(frequency>538.808f && frequency<=1077.616f){
            float temp_frequency;
            for(float i=2.0f; i<10.0f; i++){
                temp_frequency = frequency/i;
                if(temp_frequency>=261.626f && temp_frequency<=538.808f){
                    octave = (int)i;
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
                    octave = (int)(i-1.0f);
                    frequency = frequency/i;
                    break;
                }
            }
        }
        return frequency;
    }
}
