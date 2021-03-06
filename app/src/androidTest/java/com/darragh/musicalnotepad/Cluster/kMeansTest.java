package com.darragh.musicalnotepad.Cluster;

import com.darragh.musicalnotepad.Objects.KeySignature;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class kMeansTest {

    private static int[] getPositions(ArrayList<ClusterNode> nodes){
        int[] positions = new int[nodes.size()];
        for(int i=0; i<nodes.size(); i++){
            positions[i] = nodes.get(i).position;
        }
        return positions;
    }

    //Time to test the clusterToABCFormat Converter.
    @Test
    public void sortOctave(){
        String[] input = {"_A2", "A2", "^A1", "A1"};
        String[] expectedOctave = {"_a", "a", "^A", "A"};
        for(int i=0; i<input.length; i++){
            assertEquals(clusterToABCFormat.sortOctave(input[i]), expectedOctave[i]);
        }
    }

    //I am going to test a basic noteProgression using the cluster below.
    @Test
    public void testABCFormat(){
        assertEquals(kMeans.main(convergence_testCluster(),new KeySignature("DMajor"),8),"^G^aF=f2=C2(c|c2)z3(E3|\nE)=F4(A3|A)z7|");
    }

    //Test the aggregate of k separate clusters, maintain chronological order.
    private static int[] expectedPositions(ArrayList<ClusterNode> nodes){
        int[] expected = new int[nodes.size()];
        for(int i=0; i< expected.length; i++){
            expected[i] = i;
        }
        return expected;
    }
    private static String[] getNotes(ArrayList<ClusterNode> nodes){
        String[] notes = new String[nodes.size()];
        for(int i=0; i<notes.length; i++){
            notes[i] = nodes.get(i).note;
        }
        return notes;
    }

    private static String[] expectedNotes(ArrayList<ClusterNode> nodes){
        String[] notes = new String[nodes.size()];
        for(int i=0; i<notes.length; i++){
            notes[i] = nodes.get(i).note;
        }
        return notes;
    }

    @Test
    public void aggregateCluster(){
        ArrayList<ClusterNode> aggregated = kMeansMain_noABCFormat(convergence_testCluster());
        assertArrayEquals(getPositions(aggregated),expectedPositions(aggregated));
        assertArrayEquals(getNotes(aggregated),expectedNotes(aggregated));
    }


    //Testing termination of KMeans over multiple iteration. Example give here deals with an infinite
    //loop.
    public static ArrayList<ClusterNode> convergence_testCluster(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"_A1"));
        testCluster.add(new ClusterNode(1,6,"^A2"));
        testCluster.add(new ClusterNode(2,7,"^F1"));
        testCluster.add(new ClusterNode(3,10,"F2"));
        testCluster.add(new ClusterNode(4,12,"C1"));
        testCluster.add(new ClusterNode(5,14,"^C2"));
        testCluster.add(new ClusterNode(6,15,"SPACE"));
        testCluster.add(new ClusterNode(7,18,"E1"));
        testCluster.add(new ClusterNode(8,20,"F1"));
        testCluster.add(new ClusterNode(9,21,"A1"));
        return testCluster;
    }

    private static int[] lengths_expectedClusterOne(){
        int[] expected = {5,6,7};
        return expected;
    }

    private static int[] lengths_expectedClusterTwo(){
        int[] expected = {10,12};
        return expected;
    }

    private static int [] lengths_expectedClusterThree(){
        int[] expected = {14,15};
        return expected;
    }

    private static int[] lengths_expectedClusterFour(){
        int[] expected = {18,20,21};
        return expected;
    }

    private ArrayList<ClusterNode> kMeansMain_noABCFormat(ArrayList<ClusterNode> node){
        kMeans.initiateKMeans(node);
        kMeans.iterateKMeans();
        int counter=0;
        while(!kMeans.convergence() && counter<7){
            kMeans.saveCentroids();
            kMeans.iterateKMeans();
            counter++;
        }
        return kMeans.aggregateCluster();
    }

    @Test
    public void convergence(){
        ArrayList<ClusterNode> aggregated = kMeansMain_noABCFormat(convergence_testCluster());
        //Cluster1
        assertArrayEquals(extractLengths(0),lengths_expectedClusterOne());
        assertArrayEquals(extractClusterNumbers(0,aggregated),expected_extractClusterOneNumbers());
        //Cluster2
        assertArrayEquals(extractLengths(1),lengths_expectedClusterTwo());
        assertArrayEquals(extractClusterNumbers(1,aggregated),expected_extractClusterTwoNumbers());
        //Cluster3
        assertArrayEquals(extractLengths(2),lengths_expectedClusterThree());
        assertArrayEquals(extractClusterNumbers(2,aggregated),expected_extractClusterThreeNumbers());
        //Cluster4
        assertArrayEquals(extractLengths(3),lengths_expectedClusterFour());
        assertArrayEquals(extractClusterNumbers(3,aggregated),expected_extractClusterFourNumbers());
    }

    //----Test the Assignment values to the closest cluster, marking the cluster flag in the process.
    private static ArrayList<ClusterNode> fillClosest_OneCluster(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,18,"A"));
        testCluster.add(new ClusterNode(1,20,"A"));
        testCluster.add(new ClusterNode(2,5,"A"));
        testCluster.add(new ClusterNode(3,6,"A"));
        testCluster.add(new ClusterNode(4,12,"A"));
        testCluster.add(new ClusterNode(5,14,"A"));
        testCluster.add(new ClusterNode(6,15,"A"));
        testCluster.add(new ClusterNode(7,7,"A"));
        testCluster.add(new ClusterNode(8,10,"A"));
        testCluster.add(new ClusterNode(9,21,"A"));
        return testCluster;
    }

    private static int[] extractLengths(int clusterNumber){
        int[] lengths = new int[kMeans.getClusterList().get(clusterNumber).clusterList.size()];
        for(int i=0; i<lengths.length; i++){
            lengths[i] = kMeans.getClusterList().get(clusterNumber).clusterList.get(i).length;
        }
        return lengths;
    }

    private static int[] extractLengths(int clusterNumber, ArrayList<ClusterNode> nodes){
        ArrayList<Integer> lengths = new ArrayList<>();
        for(ClusterNode node: nodes){
            if(node.cluster==clusterNumber+1){
                lengths.add(node.cluster);
            }
        }
        int[] actual = new int[lengths.size()];
        for(int i=0; i<actual.length; i++){
            actual[i] = lengths.get(i);
        }
        return actual;
    }


    //Rewrite this method

    private static int[] extractClusterNumbers(int clusterNumber, ArrayList<ClusterNode> nodes){
        ArrayList<Integer> actualOutput = new ArrayList<>();
        for(ClusterNode node: nodes){
            if(node.cluster==clusterNumber+1){
                actualOutput.add(clusterNumber+1);
            }
        }
        int[] actual = new int[actualOutput.size()];
        for(int i=0; i<actual.length; i++){
            actual[i] = actualOutput.get(i);
        }
        return actual;
    }

    private static int[] extractClusterNumbers(int clusterNumber){
        int[] numbers = new int[kMeans.getClusterList().get(clusterNumber).clusterList.size()];


        for(int i=0; i<numbers.length; i++){
            numbers[i] = kMeans.getClusterList().get(clusterNumber).clusterList.get(i).cluster;
        }
        return numbers;
    }

    private static int[] expected_extractLengthClusterOne(){
        int[] expected = {5,6,7};
        return expected;
    }

    private static int[] expected_extractClusterOneNumbers(){
        int[] expected = {1,1,1};
        return expected;
    }

    private static int[] expected_extractLengthClusterTwo(){
        int[] expected = {12,10};
        return expected;
    }

    private static int[] expected_extractClusterTwoNumbers(){
        int[] expected = {2,2};
        return expected;
    }

    private static int[] expected_extractLengthClusterThree(){
        int[] expected = {14,15};
        return expected;
    }

    private static int[] expected_extractClusterThreeNumbers(){
        int[] expected = {3,3};
        return expected;
    }

    private static int[] expected_extractLengthClusterFour(){
        int[] expected = {18,20,21};
        return expected;
    }

    private static int[] expected_extractClusterFourNumbers(){
        int[] expected = {4,4,4};
        return expected;
    }

    @Test
    public void AssignClosest_FourCluster(){
        kMeans.initiateKMeans(fillClosest_OneCluster());
        kMeans.iterateKMeans();
        kMeans.recalibrateCentroids();
        //Cluster1 with new centroid check.
        assertArrayEquals(extractLengths(0),expected_extractLengthClusterOne());
        assertArrayEquals(extractClusterNumbers(0),expected_extractClusterOneNumbers());
        assertEquals(6.0,kMeans.getClusterList().get(0).centroid,0);
        //Cluster2 with new centroid check.
        assertArrayEquals(extractLengths(1),expected_extractLengthClusterTwo());
        assertArrayEquals(extractClusterNumbers(1),expected_extractClusterTwoNumbers());
        assertEquals(11.0,kMeans.getClusterList().get(1).centroid,0);
        //Cluster3 with new centroid check.
        assertArrayEquals(extractLengths(2),expected_extractLengthClusterThree());
        assertArrayEquals(extractClusterNumbers(2),expected_extractClusterThreeNumbers());
        assertEquals(14.5,kMeans.getClusterList().get(2).centroid,0);
        //Cluster4 with new centroid check.
        assertArrayEquals(extractLengths(3),expected_extractLengthClusterFour());
        assertArrayEquals(extractClusterNumbers(3),expected_extractClusterFourNumbers());
        assertEquals(19.66666666,kMeans.getClusterList().get(3).centroid,.01);

    }

    //----Initiation of Clusters-----

    public ArrayList<ClusterNode> setOneCluster(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"A"));
        testCluster.add(new ClusterNode(1,7,"A"));
        testCluster.add(new ClusterNode(2,8,"A"));
        testCluster.add(new ClusterNode(3,9,"A"));
        testCluster.add(new ClusterNode(4,10,"A"));
        testCluster.add(new ClusterNode(5,6,"A"));
        testCluster.add(new ClusterNode(6,7,"A"));
        return testCluster;
    }

    @Test
    public void initiate_OneCluster() throws Exception {
        ArrayList<ClusterNode> oneClusterTest = setOneCluster();
        kMeans.initiateKMeans(oneClusterTest);
        assertEquals(1,kMeans.getNumberOfClusters());
        assertEquals(5,kMeans.lowestValue());
        assertEquals(10,kMeans.highestValue());
        assertEquals(5.0,kMeans.getClusterList().get(0).centroid,0);
    }

    public ArrayList<ClusterNode> setTwoClusters(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"A"));
        testCluster.add(new ClusterNode(1,7,"A"));
        testCluster.add(new ClusterNode(2,8,"A"));
        testCluster.add(new ClusterNode(3,9,"A"));
        testCluster.add(new ClusterNode(4,11,"A"));
        testCluster.add(new ClusterNode(5,6,"A"));
        testCluster.add(new ClusterNode(6,7,"A"));
        return testCluster;
    }

    @Test
    public void initiate_TwoCluster() throws Exception {
        ArrayList<ClusterNode> oneClusterTest = setTwoClusters();
        kMeans.initiateKMeans(oneClusterTest);
        assertEquals(2,kMeans.getNumberOfClusters());
        assertEquals(5,kMeans.lowestValue());
        assertEquals(11,kMeans.highestValue());
        assertEquals(5.0,kMeans.getClusterList().get(0).centroid,0);
        assertEquals(10.0,kMeans.getClusterList().get(1).centroid,0);
    }

    public ArrayList<ClusterNode> setThreeClusters(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"A"));
        testCluster.add(new ClusterNode(1,7,"A"));
        testCluster.add(new ClusterNode(2,8,"A"));
        testCluster.add(new ClusterNode(3,16,"A"));
        testCluster.add(new ClusterNode(4,11,"A"));
        testCluster.add(new ClusterNode(5,6,"A"));
        testCluster.add(new ClusterNode(6,7,"A"));
        return testCluster;
    }

    @Test
    public void initiate_ThreeCluster() throws Exception {
        ArrayList<ClusterNode> oneClusterTest = setThreeClusters();
        kMeans.initiateKMeans(oneClusterTest);
        assertEquals(3,kMeans.getNumberOfClusters());
        assertEquals(5,kMeans.lowestValue());
        assertEquals(16,kMeans.highestValue());
        assertEquals(5.0,kMeans.getClusterList().get(0).centroid,0);
        assertEquals(10.0,kMeans.getClusterList().get(1).centroid,0);
        assertEquals(15.0,kMeans.getClusterList().get(2).centroid,0);
    }

    public ArrayList<ClusterNode> setFourClusters(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"A"));
        testCluster.add(new ClusterNode(1,7,"A"));
        testCluster.add(new ClusterNode(2,8,"A"));
        testCluster.add(new ClusterNode(3,21,"A"));
        testCluster.add(new ClusterNode(4,11,"A"));
        testCluster.add(new ClusterNode(5,6,"A"));
        testCluster.add(new ClusterNode(6,7,"A"));
        return testCluster;
    }

    @Test
    public void initiate_FourCluster() throws Exception {
        ArrayList<ClusterNode> oneClusterTest = setFourClusters();
        kMeans.initiateKMeans(oneClusterTest);
        assertEquals(4,kMeans.getNumberOfClusters());
        assertEquals(5,kMeans.lowestValue());
        assertEquals(21,kMeans.highestValue());
        assertEquals(5.0,kMeans.getClusterList().get(0).centroid,0);
        assertEquals(10.0,kMeans.getClusterList().get(1).centroid,0);
        assertEquals(15.0,kMeans.getClusterList().get(2).centroid,0);
        assertEquals(20.0,kMeans.getClusterList().get(3).centroid,0);
    }


    //------------------------------


}