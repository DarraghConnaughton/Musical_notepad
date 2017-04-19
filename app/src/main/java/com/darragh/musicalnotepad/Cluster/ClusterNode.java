package com.darragh.musicalnotepad.Cluster;

public class ClusterNode {
    public int position,length, cluster;
    public String note;

    public ClusterNode(int cnPosition,int cnLength,String cnNote){
        position = cnPosition;
        length = cnLength;
        note = cnNote;
    }
    public void printNode(){
        System.out.println(position + " - " + note + " - " + length + " - " + cluster);
    }
}
