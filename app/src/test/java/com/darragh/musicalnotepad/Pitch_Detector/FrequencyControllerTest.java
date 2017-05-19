package com.darragh.musicalnotepad.Pitch_Detector;

import com.darragh.musicalnotepad.Objects.Frequency;

import org.junit.Test;

import static org.junit.Assert.*;

public class FrequencyControllerTest {
    @Test
    public void scaleFrequencyDown() throws Exception {
        assertEquals(FrequencyController.scaleFrequencyDown(new Frequency(538.809f,1)).getFrequency(),269.404f,0.1);
        assertEquals(FrequencyController.scaleFrequencyDown(new Frequency(538.809f,1)).getOctave(),2,0.1);
        assertEquals(FrequencyController.scaleFrequencyDown(new Frequency(1077.616f,1)).getFrequency(),538.808f,0.1);
        assertEquals(FrequencyController.scaleFrequencyDown(new Frequency(1077.616f,1)).getOctave(),2,0.1);
    }

}