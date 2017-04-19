package com.darragh.musicalnotepad;

import org.junit.Test;

import jm.midi.event.KeySig;

import static org.junit.Assert.*;


public class KeySignatureTest {

    public String[] initiateFlats(){
        String[] flats = {"_A1","_B1","_D1","_E1","_G1"};
        return flats;
    }

    public String[] sharp_flat(String[] flats){
        for(int i=0; i<flats.length; i++){
            flats[i] = KeySignature.flat_to_sharp(flats[i]);
            System.out.println(flats[i]);
        }
        return flats;
    }

    public String[] expectedSharps(){
        String[] sharps = {"^G1","^A1","^C1","^D1","^F1"};
        return sharps;
    }

    @Test
    public void flat_to_sharp() throws Exception {
        assertArrayEquals(expectedSharps(),sharp_flat(initiateFlats()));
    }

}