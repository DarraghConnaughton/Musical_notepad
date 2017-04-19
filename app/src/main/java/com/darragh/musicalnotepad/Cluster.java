package com.darragh.musicalnotepad;

import java.util.ArrayList;

public class Cluster {
    public double centroid;
    public ArrayList<ClusterNode> clusterList;

    Cluster(double cCentroid){
        centroid = cCentroid;
        clusterList = new ArrayList<ClusterNode>();
    }
    void printClusterDetails(){
        System.out.println("Cluster size -> " + clusterList.size());
        for(ClusterNode node: clusterList){
            node.printNode();
        }
    }
//    public static void setCentroid(double cCentroid){
//        centroid=cCentroid;
//    }
//    public static void addNode(ClusterNode cNode){
//        clusterList.add(cNode);
//    }
//    public static ArrayList<ClusterNode> getCluster(){
//        return clusterList;
//    }
//    public static double getCentroid(){
//        return centroid;
//    }
//    public static void recalibrate(){
//        int totalValue=0;
//        for(ClusterNode node: clusterList){
//            totalValue=totalValue+node.length;
//        }
//        centroid = totalValue/clusterList.size();
//    }

//    public static void printCluster(int clusterNumber){
//        System.out.println("Cluster number: " + clusterNumber);
//        for(ClusterNode node: clusterList){
//            System.out.println(node.length);
//        }
//    }

//    public static void resetCluster(){
//        clusterList = new ArrayList<ClusterNode>();
//    }
}
