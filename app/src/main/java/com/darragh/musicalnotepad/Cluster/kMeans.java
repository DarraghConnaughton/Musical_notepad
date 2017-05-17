package com.darragh.musicalnotepad.Cluster;

import com.darragh.musicalnotepad.Objects.KeySignature;

import java.util.ArrayList;

public class kMeans {
    private static ArrayList<ClusterNode> clusterNodeSet;
    private static int numberOfClusters;
    private static double lowest, highest;
    private static ArrayList<Cluster> clusterList;
    private static double[] previousCentroids;

    //For testing purposes
    private static ArrayList<ClusterNode> aggregatedClusterTest;
    public static int getNumberOfClusters(){
        return numberOfClusters;
    }
    public static ArrayList<Cluster> getClusterList(){
        return clusterList;
    }
    public static ArrayList<ClusterNode> getAggregatedCluster(){ return aggregatedClusterTest; }


    //-----------------------
    public static void setClusterNodeSet(ArrayList<ClusterNode> initClusterNodeSet){
        clusterNodeSet = initClusterNodeSet;
    }

    public static int lowestValue(){
        int minValue =clusterNodeSet.get(0).length;
        for(ClusterNode node: clusterNodeSet){
            if(node.length<minValue){
                minValue = node.length;
            }
        }
        return minValue;
    }

    public static int highestValue(){
        int maxValue = clusterNodeSet.get(0).length;
        for(ClusterNode node: clusterNodeSet){
            if(node.length>maxValue){
                maxValue=node.length;
            }
        }
        return maxValue;
    }

    public static void determineNumberOfClusters(){
        for(int i=1; i<5; i++){
            if(lowest*i<highest){
                numberOfClusters = i;
            }
        }
    }

    public static void setMinMax(){
        lowest = lowestValue();
        highest = highestValue();
    }

    public static void setCentroids(){
        clusterList = new ArrayList<>();
        for(int i=0; i<numberOfClusters; i++){
            clusterList.add(new Cluster(lowest*(i+1)));
        }
    }

    public static void resetClusterLists(){
        for(int i=0; i<numberOfClusters; i++){
            clusterList.get(i).clusterList=new ArrayList<>();
        }
    }

    public static double getDistance(int noteLength, double centroid){
        return Math.abs(centroid-(double)noteLength);
    }

    public static int closestCluster(double[] distances){
        if(distances.length>0){
            double min = distances[0];
            int minIndex=0;
            for(int i=0; i<distances.length; i++){
                if(min>distances[i]){
                    min = distances[i];
                    minIndex = i;
                }
            }
            return minIndex;
        }
        return 0;
    }

    public static void fillClusters(){
        int[] clusterPositions = new int[numberOfClusters];
        for(ClusterNode node: clusterNodeSet){
            double[] distances = new double[numberOfClusters];
            for(int i=0; i<numberOfClusters; i++){
                distances[i] = getDistance(node.length,clusterList.get(i).centroid);
            }
            int closest = closestCluster(distances);
            clusterList.get(closest).clusterList.add(node);
            clusterPositions[closest]++;
            clusterList.get(closest).clusterList.get(clusterPositions[closest]-1).cluster=closest+1;
        }

    }

    private static void calculateCentroid(int clusterNumber){
        int total=0;
        for(ClusterNode node: clusterList.get(clusterNumber).clusterList){
            total=total+node.length;
        }
        clusterList.get(clusterNumber).centroid=(double)total/clusterList.get(clusterNumber).clusterList.size();
    }

    public static void recalibrateCentroids(){
        for(int i=0; i<numberOfClusters; i++){
            calculateCentroid(i);
        }
    }

    public static void saveCentroids(){
        previousCentroids = new double[numberOfClusters];
        for(int i=0; i<numberOfClusters; i++){
            previousCentroids[i] = clusterList.get(i).centroid;
        }
    }

    public static void iterateKMeans(){
        saveCentroids();
        resetClusterLists();
        fillClusters();
        recalibrateCentroids();
    }

    public static void initiateKMeans(ArrayList<ClusterNode> initClusterNodeSet){
        setClusterNodeSet(initClusterNodeSet);
        setMinMax();
        determineNumberOfClusters();
        setCentroids();
    }

    public static boolean convergence(){
        for(int i=0; i<numberOfClusters; i++){
            if(previousCentroids[i]!=clusterList.get(i).centroid){
                return false;
            }
        }
        return true;
    }

    private static int correctCluster(int position, int[] positions){
        for(int i=0; i<positions.length; i++){
            if(positions[i]==position){
                return i;
            }
        }
        return 0;
    }

    public static ArrayList<ClusterNode> aggregateCluster(){
        int[] clusterPositions = new int[numberOfClusters];
        ArrayList<ClusterNode> aggregatedCluster = new ArrayList<>();
        for(int i=0; i<clusterNodeSet.size(); i++){
            int[] positions = new int[numberOfClusters];
            for(int j=0; j<numberOfClusters; j++){
                if(clusterList.get(j).clusterList.size()>clusterPositions[j]){
                    positions[j] = clusterList.get(j).clusterList.get(clusterPositions[j]).position;
                }
                else{
                    positions[j] = -1;
                }
            }
            aggregatedCluster.add(clusterList.get(correctCluster(i,positions)).clusterList.get(clusterPositions[correctCluster(i,positions)]));
            clusterPositions[correctCluster(i,positions)]++;
        }
        return aggregatedCluster;
    }

    public static String main(ArrayList<ClusterNode> initClusterNodeSet, KeySignature keySignature,int timesignature){
        initiateKMeans(initClusterNodeSet);
        iterateKMeans();
        int counter=0;
        while(!convergence() && counter<7){
            saveCentroids();
            iterateKMeans();
            counter++;
        }
        return clusterToABCFormat.formatCluster(aggregateCluster(),keySignature,timesignature);
    }
}
