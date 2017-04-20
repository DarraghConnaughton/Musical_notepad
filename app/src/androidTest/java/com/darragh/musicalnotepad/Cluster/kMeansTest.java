package com.darragh.musicalnotepad.Cluster;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by darragh on 20/04/17.
 */
public class kMeansTest {

    @Test
    public void setClusterNodeSet() throws Exception {

    }

    @Test
    public void lowestValue() throws Exception {

    }

    @Test
    public void highestValue() throws Exception {

    }

    @Test
    public void determineNumberOfClusters() throws Exception {

    }

    @Test
    public void setMinMax() throws Exception {

    }

    //----Test the Assignment values to the closest cluster, marking the cluster flag in the process.
    private static ArrayList<ClusterNode> fillClosest_OneCluster(){
        ArrayList<ClusterNode> testCluster = new ArrayList<>();
        testCluster.add(new ClusterNode(0,5,"A"));
        testCluster.add(new ClusterNode(1,6,"A"));
        testCluster.add(new ClusterNode(2,7,"A"));
        testCluster.add(new ClusterNode(3,10,"A"));
        testCluster.add(new ClusterNode(4,12,"A"));
        testCluster.add(new ClusterNode(5,14,"A"));
        testCluster.add(new ClusterNode(6,15,"A"));
        testCluster.add(new ClusterNode(7,18,"A"));
        testCluster.add(new ClusterNode(8,20,"A"));
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

    private int[] extractClusterNumbers(int clusterNumber){
        int[] numbers = new int[kMeans.getClusterList().get(clusterNumber).clusterList.size()];
        for(int i=0; i<numbers.length; i++){
            numbers[i] = kMeans.getClusterList().get(clusterNumber).clusterList.get(i).cluster;
        }
        return numbers;
    }

    private int[] expected_extractLengthClusterOne(){
        int[] expected = {5,6,7};
        return expected;
    }

    private int[] expected_extractClusterOneNumbers(){
        int[] expected = {1,1,1};
        return expected;
    }

    private int[] expected_extractLengthClusterTwo(){
        int[] expected = {10,12};
        return expected;
    }

    private int[] expected_extractClusterTwoNumbers(){
        int[] expected = {2,2};
        return expected;
    }

    private int[] expected_extractLengthClusterThree(){
        int[] expected = {14,15};
        return expected;
    }

    private int[] expected_extractClusterThreeNumbers(){
        int[] expected = {3,3};
        return expected;
    }

    private int[] expected_extractLengthClusterFour(){
        int[] expected = {18,20,21};
        return expected;
    }

    private int[] expected_extractClusterFourNumbers(){
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

    @Test
    public void initiateKMeans() throws Exception {

    }

    @Test
    public void main() throws Exception {

    }

}