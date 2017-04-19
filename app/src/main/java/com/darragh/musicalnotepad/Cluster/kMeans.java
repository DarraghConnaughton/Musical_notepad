package com.darragh.musicalnotepad.Cluster;

import com.darragh.musicalnotepad.Pitch_Detector.KeySignature;

import java.util.ArrayList;

public class kMeans {
    private static Cluster cluster1,cluster2,cluster3;
    private static ArrayList<ClusterNode> clusterSet;
    private static double prevCentroid1, prevCentroid2, prevCentroid3;
    private static KeySignature keySignature;

    public static double meanValue(){
        double mean=0;
        for(ClusterNode node: clusterSet){
            mean =  mean + (double) node.length;
        }
        return mean/(double)clusterSet.size();
    }

    public static void setKeySignature(KeySignature key){
        keySignature = key;
    }

    public static Cluster getCluster(int number){
        switch(number){
//            case 1:
//                return cluster1;
            case 2:
                return cluster2;
            case 3:
                return cluster3;
        }
        return null;
    }


    public static double lowestValue(){
        int minValue =clusterSet.get(0).length;
        for(ClusterNode node: clusterSet){
            if(node.length<minValue){
                minValue = node.length;
            }
        }
        return minValue;
    }

    public static double maxValue(){
        int maxValue = clusterSet.get(0).length;
        for(ClusterNode node: clusterSet){
            if(node.length>maxValue){
                maxValue=node.length;
            }
        }
        return maxValue;
    }

    public static int shortestDistance(int length){
        double meanDist = Math.abs(cluster2.centroid-(double)length);
        double highDist = Math.abs(cluster3.centroid-(double)length);

        if(meanDist<=highDist){
            return 2;
        } else {
            return 3;
        }
// Code for 3 Clusters
//        if(lowDist<=meanDist && lowDist<=highDist){
//            return 1;
//        } else if(meanDist<=lowDist && meanDist<=highDist){
//            return 2;
//        } else {
//            return 3;
//        }
     }

    public static void iterationKMeans(){
        for(ClusterNode node: clusterSet){
            switch(shortestDistance(node.length)){
//                case 1:
//                    System.out.println("Case 1");
//                    cluster1.clusterList.add(node);
//                    break;
                case 2:
//                    System.out.println("Case 2");
                    cluster2.clusterList.add(node);
                    break;
                case 3:
//                    System.out.println("Case 3");
                    cluster3.clusterList.add(node);
                    break;
            }
        }
    }

    public static void initiateClusters(double low, double high, double mean){
//        cluster1 = new Cluster(low);
        cluster2 = new Cluster(mean);
        cluster3 = new Cluster(high);
    }

    public static void storePrevCentroid(){
//        prevCentroid1=cluster1.centroid;
        prevCentroid2=cluster2.centroid;
        prevCentroid3=cluster3.centroid;
    }
    public static void recalibrateClusters(){
//        recalibrate(cluster1);
        recalibrate(cluster2);
        recalibrate(cluster3);
    }

    public static void recalibrate(Cluster cluster){
        int totalValue=0;
        for(ClusterNode node: cluster.clusterList){
            totalValue=totalValue+node.length;
        }
        cluster.centroid = (double)totalValue/(double)cluster.clusterList.size();
    }

    public static boolean convergence(){

//        System.out.println("Convergence: " + prevCentroid2==cluster2.centroid + " - " + (prevCentroid3==cluster3.centroid));
        if(prevCentroid2==cluster2.centroid
                && prevCentroid3==cluster3.centroid){
            return true;
        } else {
            return false;
        }
    }

    public static void resetClusters(){
//        cluster1.clusterList = new ArrayList<ClusterNode>();
        cluster2.clusterList = new ArrayList<ClusterNode>();
        cluster3.clusterList = new ArrayList<ClusterNode>();
    }

    public static void printCluster(ArrayList<ClusterNode> clusterList){
        for(ClusterNode node: clusterList){
            System.out.println(node.length + " - " + node.note + " - position: " + node.position);
        }
    }

    public static String sortAccidentals(KeySignature key, String note){
        System.out.println("Sort Accidentals");
        if(key.getType().equals("sharp")){
            if(key.getNotes().contains(key.flat_to_sharp(note).substring(0,note.length()-1))) {
                System.out.println(key.flat_to_sharp(note).substring(1, note.length()));
                return key.flat_to_sharp(note).substring(1, note.length());
            } else {
                System.out.println(key.flat_to_sharp(note));
                return key.flat_to_sharp(note);
            }
        } else if(key.getType().equals("flat")){
            if(key.getNotes().contains(note.substring(0,note.length()-1))){
                System.out.println(note.substring(1, note.length()));
                return note.substring(1, note.length());
            }
        }
        System.out.println(note);
        return note;
    }

    public static void setClusterSet(ArrayList<ClusterNode> cClusterSet){
        clusterSet=cClusterSet;
    }

    public static boolean containsAccidental(ClusterNode node){
        if(node.note.substring(0,1).equals("^") || node.note.substring(0,1).equals("_")){
            return true;
        }
        else return false;
    }

    public static String findOctave(ClusterNode node){
        String currentOctave = node.note.replaceAll("[^0-9]","");

        System.out.println("CURRENT OCTAVE -> " + currentOctave);
        return currentOctave;
    }

    public static String setLength(ClusterNode node){
        if(node.cluster==2){
            return "";
        } else {
            return "2";
        }
    }

    public static int incrementBeat(ClusterNode node){
        System.out.println("---- " + node.cluster);
        if(node.cluster==2){
            return 1;
        } else if(node.cluster==3){
            return 2;
        }
        return 0;
    }

    public static String clusterToNoteProgression(ArrayList<ClusterNode> cClusterSet){
        int beatCounter = 0;
        int barCounter = 0;
        String noteProgression = "|: ";

//        for(ClusterNode node: cClusterSet){
//            if(beatCounter>=8){
//                noteProgression = noteProgression + "|";
//                beatCounter=0;
//                barCounter++;
//            }
//            if(barCounter==2){
//                noteProgression = noteProgression + "\n";
//                barCounter=0;
//            }
//            if(node.note.equals("SPACE")){
//                noteProgression = noteProgression + "z";
//                beatCounter++;
//            } else if(){
//
//            }
//        }
        System.out.println("Notes in aggregated cluster. noteProgressionPrint");
        for(ClusterNode node: cClusterSet){
            System.out.println(node.note);
        }

        for(ClusterNode node: cClusterSet){
//            System.out.println(beatCounter);
            if(beatCounter>=8){
                noteProgression = noteProgression + "|";
                beatCounter=0;
                barCounter++;
            }
            if(barCounter==2){
                noteProgression = noteProgression + "\n";
                barCounter=0;
            }
            if(node.note.equals("SPACE")){
                noteProgression = noteProgression + "z";
                beatCounter++;
            }
            else if(containsAccidental(node)){

                String currentOctave = findOctave(node);
                if(currentOctave.equals("1")){
                    noteProgression = noteProgression + sortAccidentals(keySignature,node.note).substring(0,node.note.length()-1) + setLength(node);
                    beatCounter = beatCounter + incrementBeat(node);
                } else {
                    noteProgression = noteProgression + sortAccidentals(keySignature,node.note).substring(0,node.note.length()-1).toLowerCase() + setLength(node);
                    beatCounter = beatCounter + incrementBeat(node);
                }
//                noteProgression = noteProgression + sortAccidentals(keySignature,node) +
            }
            else {
                String currentOctave = findOctave(node);
                if(currentOctave.equals("1")){
                    noteProgression = noteProgression + node.note.substring(0,node.note.length()-1) + setLength(node);
                    beatCounter = beatCounter + incrementBeat(node);
                }
                else {
                    noteProgression = noteProgression + node.note.substring(0,node.note.length()-1).toLowerCase() + setLength(node);
                    beatCounter = beatCounter + incrementBeat(node);
                }
            }
            System.out.println(noteProgression + " - Current note: " + node.note);
        }
        noteProgression = noteProgression + " :|";
        System.out.println("*************\n" + noteProgression);
        return noteProgression;
    }

    public static ArrayList<ClusterNode> aggregateCluster(){
        ArrayList<ClusterNode> orderCluster = new ArrayList<ClusterNode>();
        System.out.println("Cluster 2");
        for(ClusterNode node: cluster2.clusterList){
            System.out.println(node.note);
        }
        System.out.println("Cluster 3");
        for(ClusterNode node: cluster3.clusterList){
            System.out.println(node.note);
        }
        int point2 = 0;
        int point3 = 0;
        System.out.println("First loop");

        for(int i=0; i<cluster2.clusterList.size()+cluster3.clusterList.size()-1; i++){
            if(cluster2.clusterList.size()>i && cluster2.clusterList.get(point2).position==i){
                orderCluster.add(cluster2.clusterList.get(point2));
                orderCluster.get(point2).cluster=2;
                point2++;
            } else if(cluster3.clusterList.size()>i && cluster3.clusterList.get(point3).position==i){
                orderCluster.add(cluster3.clusterList.get(point3));
                orderCluster.get(point3).cluster=3;
                point3++;
            }
        }
        System.out.println("*********************************");
        System.out.println("*********************************");
        if(cluster3.clusterList.size()>point3){
            orderCluster.add(cluster3.clusterList.get(point3));
            orderCluster.get(orderCluster.size()-1).cluster=3;
        } else if(cluster2.clusterList.size()>point2){
            orderCluster.add(cluster2.clusterList.get(point2));
            orderCluster.get(orderCluster.size()-1).cluster = 2;
        }
        for(ClusterNode node: orderCluster){
            System.out.println(node.note);
        }
        return orderCluster;
    }

    public static String kMeanController(ArrayList<ClusterNode> cClusterSet,KeySignature keySignature){
        setClusterSet(cClusterSet);
        for(ClusterNode node: clusterSet){
            System.out.println(node.note + " : the beginning.");
        }
        setKeySignature(keySignature);
        initiateClusters(lowestValue(),maxValue(),meanValue());
        storePrevCentroid();
        iterationKMeans();
        recalibrateClusters();
        int counter =0;
        while(!convergence()){
            storePrevCentroid();
            resetClusters();
            iterationKMeans();
            recalibrateClusters();
            counter++;
            if(counter>=5){
                break;
            }
        }
        ArrayList<ClusterNode> orderedClusterSet = aggregateCluster();
        return clusterToNoteProgression(orderedClusterSet);
    }
}
