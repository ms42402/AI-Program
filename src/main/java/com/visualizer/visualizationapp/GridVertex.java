package com.visualizer.visualizationapp;

public class GridVertex {
    private int _x;
    private int _y;
    private boolean _isBlocked;

    public GridVertex(int x, int y, boolean isBlocked) {
        _x = x;
        _y = y;
        _isBlocked = isBlocked;
    }

    public void block() {
        _isBlocked = true;
    }

    public int getXAxis() {
        return _x;
    }

    public int getYAxis() {
        return _y;
    }

    public boolean isBlocked() {
        return _isBlocked;
    }

    @Override
    public String toString() {
        return  (_x + 1) + " " + (_y + 1) + " " + (_isBlocked ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GridVertex)) {
            return false;
        }

        GridVertex cmpVertex = (GridVertex)obj;

        return this.getXAxis() == cmpVertex.getXAxis()
                && this.getYAxis() == cmpVertex.getYAxis()
                && this.isBlocked() == this.isBlocked();
    }
}
