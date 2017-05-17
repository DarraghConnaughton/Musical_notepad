package com.darragh.musicalnotepad.Modules;

import org.junit.Test;

import static org.junit.Assert.*;

public class MIDIControllerTest {
    @Test
    public void convertABC() throws Exception {
        assertEquals(MIDIController.convertABC("D","|=fabcgg(f2|f)a(=f3|=f2)"),"|=fab^cgg(^f2|^f)a(=f3|=f2)");
        assertEquals(MIDIController.convertABC("F","|=baBcggf|"),"|=ba_Bcggf|");
    }

}