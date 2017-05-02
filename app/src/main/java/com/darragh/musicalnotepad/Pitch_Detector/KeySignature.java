package com.darragh.musicalnotepad.Pitch_Detector;

import java.util.Arrays;
import java.util.List;


public class KeySignature {
    private static String keySign,abcFormat,type;
    private static List<String> notes;

    public static List<String> getNotes(String key){
        if(key.equals("DMajor")){
            abcFormat="D";
            type="sharp";
            return Arrays.asList("^F","^C");
        } else if(key.equals("CMajor")){
            type="";
            abcFormat="C";
            return Arrays.asList("");
        } else if(key.equals("GMajor")){
            type="sharp";
            abcFormat="G";
            return Arrays.asList("^F");
        } else if(key.equals("AMajor")){
            type="sharp";
            abcFormat="A";
            return Arrays.asList("^F","^C","^G");
        } else if(key.equals("EMajor")){
            type="sharp";
            abcFormat="E";
            return Arrays.asList("^F","^C","^G","^D");
        } else if(key.equals("BMajor")){
            type="sharp";
            abcFormat="B";
            return Arrays.asList("^F","^C","^G","^D","^A");
        } else if(key.equals("FMajor")){
            type="flat";
            abcFormat="F";
            return Arrays.asList("_B");
        } else if(key.equals("BbMajor")){
            type="flat";
            abcFormat="Bb";
            return Arrays.asList("_B","_E");
        } else if(key.equals("EbMajor")){
            type="flat";
            abcFormat="Eb";
            return Arrays.asList("_B","_E","_A");
        } else if(key.equals("AbMajor")){
            type="flat";
            abcFormat="Ab";
            return Arrays.asList("_B","_E","_A","_D");
        } else if(key.equals("DbMajor")){
            type="flat";
            abcFormat="Db";
            return Arrays.asList("_B","_E","_A","_D","_G");
        }
        System.out.println("That's a NULL");
        return null;
    }
    public String getAbcFormat(){
        return abcFormat;
    }
    public static List<String> getNotes(){
        return notes;
    }
    public KeySignature(String key){
        keySign = key;
        notes = getNotes(key);
    }

    public static String flat_to_sharp(String note){
        System.out.println(note.substring(note.length()-1));
        switch(note.substring(0,note.length()-1)){
            case "_A":
                return "^G" + note.substring(note.length()-1);
            case "_B":
                return "^A" + note.substring(note.length()-1);
            case "_D":
                return "^C" + note.substring(note.length()-1);
            case "_E":
                return "^D" + note.substring(note.length()-1);
            case "_G":
                return "^F" + note.substring(note.length()-1);
        }
        return note;
    }

    public static String getType(){
        return type;
    }

}
