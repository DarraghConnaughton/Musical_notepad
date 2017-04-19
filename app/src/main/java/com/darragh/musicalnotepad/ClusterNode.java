package com.darragh.musicalnotepad;

/**
 * Created by darragh on 03/04/17.
 */

public class ClusterNode {
    public int position,length, cluster;
    public String note;

    ClusterNode(int cnPosition,int cnLength,String cnNote){
        position = cnPosition;
        length = cnLength;
        note = cnNote;
    }
    void printNode(){
        System.out.println(position + " - " + note + " - " + length + " - " + cluster);
    }
}
