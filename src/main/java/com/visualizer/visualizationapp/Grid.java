package com.visualizer.visualizationapp;

public class Grid {
    private final GridVertex _vertices[][];
    private final int _rows;
    private final int _cols;
    private GridVertex _startVertex;
    private GridVertex _goalVertex;

    public Grid(int x, int y) {
        _vertices = new GridVertex[y][x];
        _rows = y;
        _cols = x;
        setup();
    }

    public GridVertex getStartVertex() {
        return _startVertex;
    }

    public GridVertex getGoalVertex() {
        return _goalVertex;
    }

    public void setStartVertex(int x, int y) {
        _startVertex = new GridVertex(x, y, false);
        _vertices[y][x] = _startVertex;
    }

    public void setGoalVertex(int x, int y) {
        _goalVertex = new GridVertex(x, y, false);
        _vertices[y][x] = _goalVertex;
    }

    private void setup() {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _vertices[i][j] = new GridVertex(j, i, false);
            }
        }
    }

    public int getRows() {
        return _rows;
    }

    public int getCols() {
        return _cols;
    }

    public void add(GridVertex v) {
        _vertices[v.getYAxis()][v.getXAxis()] = v;
    }

    public void blockVertex(GridVertex v) {
        _vertices[v.getYAxis()][v.getXAxis()].block();
    }

    public GridVertex getVertexAt(int x, int y) {
        return _vertices[y][x];
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < _cols && y >= 0 && y < _rows;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                str.append(getVertexAt(j, i).toString());
                str.append(" ");
            }
            str.append("\n");
        }

        return str.toString();
    }

}
