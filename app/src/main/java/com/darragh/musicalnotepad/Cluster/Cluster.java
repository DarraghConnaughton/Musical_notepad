package com.darragh.musicalnotepad.Cluster;

import java.util.ArrayList;

public class Cluster {
    public double centroid;
    public ArrayList<ClusterNode> clusterList;

    Cluster(double cCentroid){
        centroid = cCentroid;
        clusterList = new ArrayList<ClusterNode>();
    }
}
