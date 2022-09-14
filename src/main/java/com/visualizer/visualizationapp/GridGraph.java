package com.visualizer.visualizationapp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class GridGraph {
    private int _rows;
    private int _cols;
    private GraphVertex _startVertex; // includes the start vertex
    private GraphVertex _goalVertex; // includes the goal vertex
    private VertexInfo _vertices[][]; //includes all the vertices

    public GridGraph(int x, int y) {
        _rows = y;
        _cols = x;
        _vertices = new VertexInfo[y][x];
        initGraph();
    }

    public void setStartVertex(int x, int y) {
        _startVertex = getVertex(x, y);
    }

    public GraphVertex getStartVertex() {
        return _startVertex;
    }

    public void setGoalVertex(int x, int y) {
        _goalVertex = getVertex(x, y);
    }

    public GraphVertex getGoalVertex() {
        return _goalVertex;
    }

    public int getNumRows() {
        return _rows;
    }

    public int getNumCols() {
        return _cols;
    }

    public boolean isCellBlocked(int x, int y) {
        ArrayList<GraphVertex> neighbors;
        try {
            neighbors = _vertices[y][x].getNeighbors();
        } catch (Exception e) {
            return true;
        }

        GraphVertex neighborToCheck = new GraphVertex(x+1, y+1);

        if (neighbors.contains(neighborToCheck)) {
            return false;
        }

        return true;
    }

    public boolean isVertexCornerOfBlockedCell(int x, int y) {
       ArrayList<GraphVertex> neighbors = _vertices[y][x].getNeighbors();

       if (isValid(x - 1, y - 1) && !neighbors.contains(new GraphVertex(x - 1, y - 1)))
           return true;

       if (isValid(x + 1, y + 1) && !neighbors.contains(new GraphVertex(x + 1, y + 1)))
           return true;

       if (isValid(x - 1, y + 1) && !neighbors.contains(new GraphVertex(x - 1, y + 1)))
           return true;

       if (isValid(x + 1, y - 1) && !neighbors.contains(new GraphVertex(x + 1, y - 1)))
           return true;

       return false;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < _cols && y >= 0 && y < _rows;
    }

    public GraphVertex getVertex(int x, int y) {
        return _vertices[y][x].getVertex();
    }

    public ArrayList<GraphVertex> getVertexNeighbors(int x, int y) {
        return _vertices[y][x].getNeighbors();
    }

    public ArrayList<GraphVertex> getVertexNeighbors(GraphVertex v) {
        return _vertices[v.getYAxis()][v.getXAxis()].getNeighbors();
    }

    public void addNeighbor(GraphVertex v, GraphVertex newNeighbor) {
        ArrayList<GraphVertex> neighbors = getVertexNeighbors(v);
        if (!neighbors.contains(newNeighbor) && !v.equals(newNeighbor)) {
            neighbors.add(newNeighbor);
        }
    }

    public boolean isReachableDFS(GraphVertex start, GraphVertex goal) {
        boolean visited[][] = new boolean[_rows][_cols];

        return recursiveDFS(start, goal, visited);
    }

    private boolean recursiveDFS(GraphVertex current, GraphVertex goal, boolean[][] visited) {
        boolean isReachable = false;
        if (current.equals(goal)) {
            return true;
        }

        visited[current.getYAxis()][current.getXAxis()] = true;
        ArrayList<GraphVertex> neighbors = getVertexNeighbors(current);

        for(GraphVertex n : neighbors) {
            if (!visited[n.getYAxis()][n.getXAxis()]) {
                isReachable |= recursiveDFS(n, goal, visited);
            }
        }

        return isReachable;
    }

    public boolean isReachableBFS(GraphVertex start, GraphVertex goal) {
        boolean visited[][] = new boolean[_rows][_cols];
        Queue<GraphVertex> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.getYAxis()][start.getXAxis()] = true;

        while(!queue.isEmpty()) {
            GraphVertex current = queue.poll();

            if (current.equals(goal)) {
                return true;
            }

            ArrayList<GraphVertex> neighbors = getVertexNeighbors(current);

            for (GraphVertex n : neighbors) {
                if (!visited[n.getYAxis()][n.getXAxis()]) {
                    visited[n.getYAxis()][n.getXAxis()] = true;
                    queue.add(n);
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                GraphVertex gv = new GraphVertex(j, i);
                str.append(gv.toString() + " -> ");
                ArrayList<GraphVertex> neighbors = getVertexNeighbors(j, i);
                for (GraphVertex n : neighbors) {
                    str.append(n.toString() + " -> ");
                }
                str.append("X");
                str.append("\n");
            }
        }

        return str.toString();
    }

    private void initGraph() {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _vertices[i][j] = new VertexInfo(j, i);
            }
        }
    }

}
