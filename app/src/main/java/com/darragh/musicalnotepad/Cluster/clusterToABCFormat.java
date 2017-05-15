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
        if(node==1){
            return "";
        }
        else{
            return Integer.toString(node);
        }
    }

    private static String newLineCounter(){
        if(barCounter>1){
            barCounter=0;
            return "\n";
        }
        return "";
    }

    private static String processNode(String noteProgression, ClusterNode node, KeySignature key,String leftBracket,String rightBracklet){
        if(node.note.equals("SPACE")){
            return noteProgression + leftBracket+ "z" + setNoteLength(node.cluster) + rightBracklet;
        }
        else{
            if(containsAccidental(node.note)){
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

    public static String fillExcessSpace(int timesignature){
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

        for(ClusterNode node: nodes){
            incrementBeatCounter(node.cluster);
            if(beatCounter>=timesignature){
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
