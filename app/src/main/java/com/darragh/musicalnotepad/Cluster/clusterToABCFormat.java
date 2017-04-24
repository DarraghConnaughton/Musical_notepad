package com.darragh.musicalnotepad.Cluster;

import com.darragh.musicalnotepad.Pitch_Detector.KeySignature;

import java.util.ArrayList;

public class clusterToABCFormat {
    private static int beatCounter;
    private static int barCounter;
    private static String potentialNaturalNote(KeySignature key, String note){
        for(String listNote: key.getNotes()){
            if(!listNote.equals("")){
                if(note.substring(0,1).equals(listNote.substring(1,listNote.length()))){
                    return "=";
                }
            }
        }
        return "";
    }

    private static String setNoteLength(int node){
        switch (node){
            case 1:
                return "";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
        }
        return "";
    }

    public static String barLines(String noteProgression,int timesignature){
        if(beatCounter>timesignature){
            return noteProgression+"|";
        }
        return "";
    }

    private static String newLineCounter(){
        if(barCounter>1){
            barCounter=0;
            return "\n";
        }
        return "";
    }

    private static String processNode(String noteProgression, ClusterNode node, KeySignature key,String leftBracket,String rightBracklet){
        System.out.println("ProcessNode: " + node.note);
        if(node.note.equals("SPACE")){
            return noteProgression + leftBracket+ "z" + setNoteLength(node.cluster) + rightBracklet;
        }
        else{
            if(containsAccidental(node.note)){
                System.out.println("Sort Accidental: " + sortAccidentals(key,node.note));
                return noteProgression + leftBracket + sortOctave(sortAccidentals(key,node.note)) + setNoteLength(node.cluster) + rightBracklet;
            }
            else {
                return noteProgression + leftBracket + potentialNaturalNote(key,node.note) + sortOctave(node.note) + setNoteLength(node.cluster) + rightBracklet;
            }
        }
    }

    private static void incrementBeatCounter(int length){
        beatCounter+=length;
    }

//    private static String modifyNote(String note, int length){
//        return note.substring(0,note.length()-1)+length;
//    }

    public static String fillExcessSpace(int timesignature){
        System.out.println(beatCounter + " -- TS: " + timesignature);
        if(beatCounter<timesignature){
            return "z" + setNoteLength(timesignature-beatCounter);
        }
        else {
            return "";
        }
    }

    public static String formatCluster(ArrayList<ClusterNode> nodes, KeySignature key,int timesignature){
        String noteProgression = "";
        beatCounter=0;
        System.out.println("TIMESIGNATURE: "+timesignature);

        for(ClusterNode node: nodes){
            incrementBeatCounter(node.cluster);
            System.out.println(node.note + " length -> " + node.cluster + "  beatCounter: " + beatCounter);
            if(beatCounter>=timesignature){
                System.out.println("Inside...");
                barCounter++;
                int excess_beats = beatCounter-timesignature;
                beatCounter = excess_beats;
                int fill_bar = node.cluster-excess_beats;
                if(excess_beats>0){
                    node.cluster=fill_bar;
                    noteProgression = processNode(noteProgression,node,key,"(","");
                    node.cluster=excess_beats;
                    noteProgression = noteProgression + "|" + newLineCounter();
                    noteProgression = processNode(noteProgression,node,key,"",")");
                    beatCounter = excess_beats;
                } else {
                    noteProgression = processNode(noteProgression,node,key,"","");
                    beatCounter=0;
                    noteProgression = noteProgression + "|" + newLineCounter();
                }
            }
            else {
                noteProgression = processNode(noteProgression,node,key,"","");
            }
        }
        System.out.println("noteProgression __> " + noteProgression);
        System.out.println("fillExcessSpace(timesignature) " + fillExcessSpace(timesignature));
        return noteProgression + fillExcessSpace(timesignature) + "|";
    }

    public static String sortOctave(String note){
        if(note.replaceAll("[^0-9]","").equals("1")){
            return note.substring(0,note.length()-1);
        }
        else if(note.replaceAll("[^0-9]","").equals("2")){
            return note.substring(0,note.length()-1).toLowerCase();
        }
        else{
            return note.substring(0,note.length()-1).toLowerCase()+"'";
        }
    }

    public static String sortAccidentals(KeySignature key, String note){
        if(key.getType().equals("sharp")){
            if(key.getNotes().contains(key.flat_to_sharp(note).substring(0,note.length()-1))) {
                System.out.println("key.flat_to_sharp(note).substring(1, note.length()): " + key.flat_to_sharp(note).substring(1, note.length()));
                return key.flat_to_sharp(note).substring(1, note.length());
            } else {
                System.out.println("key.flat_to_sharp(note): " + key.flat_to_sharp(note));
                return key.flat_to_sharp(note);
            }
        } else if(key.getType().equals("flat")){
            if(key.getNotes().contains(note.substring(0,note.length()-1))){
                System.out.println("key.flat_to_sharp(note): " + key.flat_to_sharp(note));
                return note.substring(1, note.length());
            }
        }
        return note;
    }

    private static boolean containsAccidental(String note){
        return (note.substring(0,1).equals("^")||note.substring(0,1).equals("_"));
    }
}
