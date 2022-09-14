package com.visualizer.visualizationapp;

import java.io.File;
import java.util.ArrayList;

public class PathFinder {
    private BinaryMinHeap<GraphVertex> _fringe;
    private ArrayList<GraphVertex> _pathList;
    private GridGraph _graph;

    public PathFinder(GridGraph graph) {
        _graph = graph;
    }

    public ArrayList<VertexInfo> generateVisibilityGraph() {
        ArrayList<VertexInfo> visGraph = new ArrayList<>();
        visGraph.add(new VertexInfo(_graph.getStartVertex().getXAxis(), _graph.getStartVertex().getYAxis()));
        visGraph.add(new VertexInfo(_graph.getGoalVertex().getXAxis(), _graph.getGoalVertex().getYAxis()));
        for (int i = 0; i < _graph.getNumRows(); i++) {
            for (int j = 0; j < _graph.getNumCols(); j++) {
                if (_graph.isVertexCornerOfBlockedCell(j, i)) {
                    visGraph.add(new VertexInfo(j, i));
                }
            }
        }

        for (int i = 0; i < visGraph.size(); i++) {
            for (int j = 0; j < visGraph.size(); j++) {
                GraphVertex v1 = visGraph.get(i).getVertex();
                GraphVertex v2 = visGraph.get(j).getVertex();

                if (!v1.equals(v2)) {
                    if (lineOfSight(v1, v2)) {
                        visGraph.get(i).getNeighbors().add(v2);
                    }
                }
            }
        }

        return visGraph;
    }

    public ArrayList<GraphVertex> aStarPath(boolean isTheta) {
        _fringe = new BinaryMinHeap<>();
        _pathList = new ArrayList<>();
        GraphVertex start = _graph.getStartVertex();
        GraphVertex goal = _graph.getGoalVertex();
        start.setParent(start);
        start.setGVal(0);
        start.calculateHeuristic(goal);
        _fringe.add(start);

        ArrayList<GraphVertex> closed = new ArrayList();
        while (!_fringe.isEmpty()) {
            GraphVertex currentVertex = _fringe.poll();

            if (currentVertex.equals(goal)) {
                pathFromGoal(currentVertex);
                return _pathList;
            }

            closed.add(currentVertex);

            for (GraphVertex n : _graph.getVertexNeighbors(currentVertex)) {
                n = _graph.getVertex(n.getXAxis(), n.getYAxis());
                if (!closed.contains(n)) {
                    if (!_fringe.contains(n)) {
                        n.setGVal(Double.POSITIVE_INFINITY);
                    }

                    if (isTheta) {
                        n.setHVal(n.calculateDistance(goal));
                        updateThetaVertex(currentVertex, n);
                    } else {
                        n.calculateHeuristic(goal);
                        updateVertex(currentVertex, n);
                    }
                }
            }

        }

        return _pathList;
    }

    private void updateVertex(GraphVertex current, GraphVertex next) {
        if ((current.getGVal() + current.calculateDistance(next)) < next.getGVal()) {
            next.setGVal(current.getGVal() + current.calculateDistance(next));

            next.setParent(current);
            if (_fringe.contains(next)) {
                _fringe.remove(next);
            }
            _fringe.add(next);
        }
    }

    private void updateThetaVertex(GraphVertex current, GraphVertex next) {
        if (lineOfSight(current.getParent(), next)) {
            updateVertex(current.getParent(), next);
        } else {
            updateVertex(current, next);
        }
    }

    private boolean lineOfSight(GraphVertex parentOfCurrent, GraphVertex next) {
        int xCurrent = parentOfCurrent.getXAxis();
        int yCurrent = parentOfCurrent.getYAxis();
        int xNext = next.getXAxis();
        int yNext = next.getYAxis();
        int diffX = xNext - xCurrent;
        int diffY = yNext - yCurrent;
        int f = 0;
        int sY, sX;

        int offsetY = 0, offsetX = 0;

        if (diffY < 0) {
            diffY = -1*diffY;
            sY = -1;
            offsetY = -1;
        } else {
            sY = 1;
        }

        if (diffX < 0) {
            diffX = -1*diffX;
            sX = -1;
            offsetX = -1;
        } else {
            sX = 1;
        }

        if (diffX >= diffY) {
            while (xCurrent != xNext) {
                f = f + diffY;

                if (f >= diffX) {
                    if (_graph.isCellBlocked(xCurrent + offsetX, yCurrent + offsetY)) {
                        return false;
                    }

                    yCurrent = yCurrent + sY;
                    f = f - diffX;
                }

                if (f != 0 &&
                        _graph.isCellBlocked(xCurrent + offsetX, yCurrent + offsetY)) {
                    return false;
                }

                if (diffY == 0 &&
                        _graph.isCellBlocked(xCurrent + offsetX, yCurrent)
                        && _graph.isCellBlocked(xCurrent + offsetX, yCurrent - 1)) {
                    return false;
                }

                xCurrent = xCurrent + sX;
            }
        } else {
            while (yCurrent != yNext) {
                f = f + diffX;

                if (f >= diffY) {
                    if (_graph.isCellBlocked(xCurrent + offsetX, yCurrent + offsetY)) {
                        return false;
                    }

                    xCurrent = xCurrent + sX;
                    f = f - diffY;
                }

                if (f != 0 &&
                        _graph.isCellBlocked(xCurrent + offsetX, yCurrent + offsetY)) {
                    return false;
                }

                if (diffX == 0 &&
                        _graph.isCellBlocked(xCurrent, yCurrent + offsetY)
                        && _graph.isCellBlocked(xCurrent - 1, yCurrent + offsetY)) {
                    return false;
                }

                yCurrent = yCurrent + sY;
            }
        }

        return true;
    }

    private void pathFromGoal(GraphVertex v) {
        _pathList.add(v);
        while (!v.equals(v.getParent())) {
            _pathList.add(v.getParent());
            v = v.getParent();
        }
    }
}

