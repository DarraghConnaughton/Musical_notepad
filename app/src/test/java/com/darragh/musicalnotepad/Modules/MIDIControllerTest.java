package com.darragh.musicalnotepad.Modules;

import com.darragh.musicalnotepad.Pitch_Detector.Song;

import org.junit.Test;

import static org.junit.Assert.*;

public class MIDIControllerTest {
    @Test
    public void convertABC() throws Exception {
//        String test = MIDIController.convertABC(new Song("test","DARRAGH","|fabcgg|","4/4","DMajor"));
        assertEquals(MIDIController.convertABC(new Song("test","DARRAGH","|=fabcgg(f2|f)a(=f3|=f2)","4/4","DMajor")),"|=fab^cgg(^f2|^f)a(=f3|=f2)");
        assertEquals(MIDIController.convertABC(new Song("test","DARRAGH","|=baBcggf|","4/4","FMajor")),"|=ba_Bcggf|");
    }

}