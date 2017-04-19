package com.darragh.musicalnotepad;

import com.darragh.musicalnotepad.Cluster.Cluster;
import com.darragh.musicalnotepad.Cluster.ClusterNode;
import com.darragh.musicalnotepad.Cluster.kMeans;
import com.darragh.musicalnotepad.Pitch_Detector.KeySignature;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class kMeansTest {

    private static ArrayList<ClusterNode> clusterArray1, clusterArray2, clusterArray3;
    private static ArrayList<Integer> clusterLength1, clusterLength2, clusterLength3;
    private static KeySignature Dmajor = new KeySignature("DMajor");
    public static ArrayList<ClusterNode> initiateTestCluster1(){
        ArrayList<ClusterNode> testClusterSet1 = new ArrayList<>();
        testClusterSet1.add(new ClusterNode(0,10,"A"));
        testClusterSet1.add(new ClusterNode(1,14,"A"));
        testClusterSet1.add(new ClusterNode(2,15,"A"));
        testClusterSet1.add(new ClusterNode(3,16,"A"));
        testClusterSet1.add(new ClusterNode(4,20,"A"));
        testClusterSet1.add(new ClusterNode(5,24,"A"));
        testClusterSet1.add(new ClusterNode(6,25,"A"));
        testClusterSet1.add(new ClusterNode(7,28,"A"));
        testClusterSet1.add(new ClusterNode(8,30,"A"));
        testClusterSet1.add(new ClusterNode(9,32,"A"));
        testClusterSet1.add(new ClusterNode(10,35,"A"));
        testClusterSet1.add(new ClusterNode(11,36,"A"));
        return testClusterSet1;
    }

//    public static void initiateClusterArray1(){
//        clusterArray1.add(new ClusterNode(0,10,"A"));
//        clusterArray1.add(new ClusterNode(1,15,"A"));
//        clusterArray1.add(new ClusterNode(2,14,"A"));
//        clusterArray1.add(new ClusterNode(3,16,"A"));
//    }
//    public static void initiateClusterArray2(){
//        clusterArray2.add(new ClusterNode(0,20,"A"));
//        clusterArray2.add(new ClusterNode(1,24,"A"));
//        clusterArray2.add(new ClusterNode(2,25,"A"));
//        clusterArray2.add(new ClusterNode(3,28,"A"));
//    }
//    public static void initiateClusterArray3(){
//        clusterArray3.add(new ClusterNode(0,30,"A"));
//        clusterArray3.add(new ClusterNode(1,32,"A"));
//        clusterArray3.add(new ClusterNode(2,35,"A"));
//        clusterArray3.add(new ClusterNode(3,36,"A"));
//    }

    public void initiateTestClusterArrays(){
        clusterLength1 = new ArrayList<Integer>(Arrays.asList(10,14,15,16));
        clusterLength2 = new ArrayList<Integer>(Arrays.asList(20,24,25,28));
        clusterLength3 = new ArrayList<Integer>(Arrays.asList(30,32,35,36));
    }

    @Test
    public void meanValue() throws Exception {
        kMeans.setClusterSet(initiateTestCluster1());
        double mean = kMeans.meanValue();
        assertEquals(mean,23.75,0);
    }

    @Test
    public void lowestValue() throws Exception {
        kMeans.setClusterSet(initiateTestCluster1());
        double lowest = kMeans.lowestValue();
        assertEquals(lowest,10.0,0);
    }

    public static void printClusterSet(){
        ArrayList<ClusterNode> test = initiateTestCluster1();
        for(ClusterNode node: test){
            System.out.println(node.length + " - " + node.position + " - " + node.note);
        }
    }

    @Test
    public void maxValue() throws Exception {
        kMeans.setClusterSet(initiateTestCluster1());
        double max = kMeans.maxValue();
        assertEquals(max,36.0,0);
    }

    @Test
    public void shortestDistance() throws Exception {

    }

    public static ArrayList<Integer> fillFromCluster(Cluster cluster){
        ArrayList<Integer> list = new ArrayList<>();
        for(ClusterNode node: cluster.clusterList){
            list.add(node.length);
        }
        return list;
    }

    public void firstIteration(){
        initiateTestClusterArrays();
        kMeans.setClusterSet(initiateTestCluster1());
        kMeans.initiateClusters(kMeans.lowestValue(),kMeans.maxValue(),kMeans.meanValue());
        kMeans.iterationKMeans();
    }

    @Test
    public void recalibrateClusters() throws Exception {
        firstIteration();
        kMeans.recalibrateClusters();
//        assertEquals(13.75,kMeans.getCluster(1).centroid,0);
        assertEquals(24.25,kMeans.getCluster(2).centroid,0);
        assertEquals(33.25,kMeans.getCluster(3).centroid,0);
    }

    @Test
    public void iterationKMeans() throws Exception {
        firstIteration();
//        ArrayList<Integer> cluster1 = fillFromCluster(kMeans.getCluster(1));
        ArrayList<Integer> cluster2 = fillFromCluster(kMeans.getCluster(2));
        ArrayList<Integer> cluster3 = fillFromCluster(kMeans.getCluster(3));
//        assertEquals(cluster1,clusterLength1);
        assertEquals(cluster2,clusterLength2);
        assertEquals(cluster3,clusterLength3);
    }

    @Test
    public void convergence() throws Exception {
        firstIteration();
        kMeans.recalibrateClusters();
        kMeans.storePrevCentroid();
        kMeans.resetClusters();
        kMeans.iterationKMeans();
        assertEquals(kMeans.convergence(),true);
    }

    public static ArrayList<ClusterNode> fillTestCluster_NP(){
        ArrayList<ClusterNode> nodeList = new ArrayList<>();
        nodeList.add(new ClusterNode(0,15,"SPACE"));
        nodeList.add(new ClusterNode(1,18,"G1"));
        nodeList.add(new ClusterNode(2,7,"A1"));
        nodeList.add(new ClusterNode(3,15,"D2"));
        nodeList.add(new ClusterNode(4,7,"B1"));
        nodeList.add(new ClusterNode(5,10,"G1"));
        nodeList.add(new ClusterNode(6,12,"E1"));
        nodeList.add(new ClusterNode(7,6,"G1"));
        nodeList.add(new ClusterNode(8,8,"G1"));
        nodeList.add(new ClusterNode(9,5,"E1"));
        nodeList.add(new ClusterNode(10,41,"D1"));
//        for(ClusterNode node: nodeList){
//            node.cluster = 2;
//        }
        return nodeList;
    }

    public ArrayList<ClusterNode> expectedResults(){
        ArrayList<ClusterNode> expected = fillTestCluster_NP();
        for(int i=0; i<expected.size()-1; i++){
            expected.get(i).cluster = 2;
        }
        expected.get(expected.size()-1).cluster = 3;
        return expected;
    }

    public String[] clusterNotes(ArrayList<ClusterNode> nodes){
        String[] notes = new String[nodes.size()];
        for(int i=0; i<notes.length; i++){
            notes[i] = nodes.get(i).note;
        }
        return notes;
    }

    public int[] clusterNumber(ArrayList<ClusterNode> nodes){
        int[] notes = new int[nodes.size()];
        for(int i=0; i<notes.length; i++){
            notes[i] = nodes.get(i).cluster;
        }
        return notes;
    }

    public int[] clusterOrder(ArrayList<ClusterNode> nodes){
        int[] notes = new int[nodes.size()];
        for(int i=0; i<notes.length; i++){
            notes[i] = nodes.get(i).position;
        }
        return notes;
    }

    @Test
    public void containsAccidental(){
        assertEquals(kMeans.containsAccidental(new ClusterNode(0,1,"_B1")),true);
        assertEquals(kMeans.containsAccidental(new ClusterNode(0,1,"^B1")),true);
        assertEquals(kMeans.containsAccidental(new ClusterNode(0,1,"B1")),false);
    }

    @Test
    public void aggregateCluster() throws Exception{
        ArrayList<ClusterNode> testArray = fillTestCluster_NP();
        kMeans.setClusterSet(testArray);
        kMeans.setKeySignature(Dmajor);
        kMeans.initiateClusters(kMeans.lowestValue(),kMeans.maxValue(),kMeans.meanValue());
        kMeans.storePrevCentroid();
        kMeans.iterationKMeans();
        kMeans.recalibrateClusters();
        int counter=0;
        while(!kMeans.convergence()){
            kMeans.storePrevCentroid();
            kMeans.resetClusters();
            kMeans.iterationKMeans();
            kMeans.recalibrateClusters();
            counter++;
            if(counter>=5){
                break;
            }
        }
        assertArrayEquals(clusterNotes(kMeans.aggregateCluster()),clusterNotes(expectedResults()));
        assertArrayEquals(clusterNumber(kMeans.aggregateCluster()),clusterNumber(expectedResults()));
        assertArrayEquals(clusterOrder(kMeans.aggregateCluster()),clusterOrder(expectedResults()));
    }

    @Test
    public void findOctave(){
        assertEquals(kMeans.findOctave(new ClusterNode(0,1,"_A1")),"1");
        assertEquals(kMeans.findOctave(new ClusterNode(1,2,"A1")),"1");
        assertEquals(kMeans.findOctave(new ClusterNode(2,3,"^A2")),"2");
    }

    @Test
    public void clusterToNoteProgression(){
        kMeans.setClusterSet(fillTestCluster_NP());
        kMeans.setKeySignature(Dmajor);
        kMeans.initiateClusters(kMeans.lowestValue(),kMeans.maxValue(),kMeans.meanValue());
        kMeans.storePrevCentroid();
        kMeans.iterationKMeans();
        kMeans.recalibrateClusters();
        int counter=0;
        while(!kMeans.convergence()){
            kMeans.storePrevCentroid();
            kMeans.resetClusters();
            kMeans.iterationKMeans();
            kMeans.recalibrateClusters();
            counter++;
            if(counter>=5){
                break;
            }
        }
        System.out.println("-- Test Printage --");
        for(ClusterNode node: kMeans.aggregateCluster()){
            node.printNode();
        }
        System.out.println(kMeans.clusterToNoteProgression(kMeans.aggregateCluster()));
    }

    public String[] sortAccidentalsTestSet(){
        String[] testData = {"_B1","_D1","^C1","^F1","A1"};
        return testData;
    }

    public KeySignature setKey(){
        return new KeySignature("DMajor");
    }

    public String[] expectedOutput_SA(){
        String[] testData = {"^A1","^C1","C1","F1","A1"};
        return testData;
    }

    @Test
    public void sortAccidentals() throws Exception {
        String[] actualOutput = sortAccidentalsTestSet();
        for(int i=0; i<actualOutput.length; i++){
            actualOutput[i] = kMeans.sortAccidentals(setKey(),actualOutput[i]);
        }
        assertArrayEquals(actualOutput,expectedOutput_SA());
    }

    @Test
    public void storePrevCentroid() throws Exception {

    }



    @Test
    public void resetClusters() throws Exception {

    }

    @Test
    public void printClusters() throws Exception {

    }


    @Test
    public void kMeanController() throws Exception {

    }

}