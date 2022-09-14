package com.visualizer.visualizationapp;

public class GraphVertex implements Comparable<GraphVertex> {
    private final int _x;
    private final int _y;
    private double _g;
    private double _h;
    private GraphVertex _parent = null;

    public GraphVertex(int x, int y) {
        _x = x;
        _y = y;
    }

    public int getXAxis() {
        return _x;
    }

    public int getYAxis() {
        return _y;
    }

    public double getGVal() { return _g; }

    public void setGVal(double gVal) {
        _g = gVal;
    }

    public double getHVal() { return _h; }

    public void setHVal(double hVal) {
        _h = hVal;
    }

    public GraphVertex getParent() {
        return _parent;
    }

    public void setParent(GraphVertex g) {
        _parent = g;
    }

    public double calculateDistance(GraphVertex vertex) {
        return Math.sqrt(Math.pow(_x - vertex.getXAxis(), 2)
                + Math.pow(_y - vertex.getYAxis(), 2));
    }

    public double calculateHeuristic(GraphVertex goalVertex) {
        double xDiff = Math.abs(_x - goalVertex._x);
        double yDiff = Math.abs(_y - goalVertex._y);

        return _h = Math.sqrt(2)*Math.min(xDiff, yDiff) + Math.max(xDiff, yDiff) - Math.min(xDiff, yDiff);
    }

    public double getFVal() { return _h + _g; }

    @Override
    public String toString() {
        return "(" + (_x + 1) + ", " + (_y + 1) + ")";
    }

    @Override
    public int compareTo(GraphVertex o) {
        int cmp = Double.compare(getFVal(), o.getFVal());
        return cmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GraphVertex)) {
            return false;
        }

        GraphVertex cmpVertex = (GraphVertex)obj;

        return this.getXAxis() == cmpVertex.getXAxis()
                && this.getYAxis() == cmpVertex.getYAxis();
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }
}
