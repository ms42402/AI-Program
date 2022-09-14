package com.visualizer.visualizationapp;

import java.util.ArrayList;

public class VertexInfo {
    private ArrayList<GraphVertex> _neighbors;
    private GraphVertex _vertex;

    public VertexInfo(int x, int y) {
        _vertex = new GraphVertex(x, y);
        _neighbors = new ArrayList<>();
    }

    public ArrayList<GraphVertex> getNeighbors() {
        return _neighbors;
    }

    public GraphVertex getVertex() {
        return _vertex;
    }
}
