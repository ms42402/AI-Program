package com.visualizer.visualizationapp;
import javafx.scene.shape.Circle;

public class GridCircle extends Circle {
    private final GraphVertex _vertex;

    public GridCircle(GraphVertex vertex) {
        super();
        _vertex = vertex;
    }

    private double getGVal() {
        return Math.round(_vertex.getGVal() * 100) / 100.0;
    }

    private double getHVal() {
        return Math.round(_vertex.getHVal() * 100) / 100.0;
    }

    private double getFVal() {
        return Math.round((getGVal() + getHVal()) * 100) / 100.0;
    }

    @Override
    public String toString() {
        return "g(n) = " + getGVal() + " h(n) = " + getHVal() + " f(n) = " + getFVal();
    }
}

