package com.visualizer.visualizationapp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class GraphFactory {

    public GridGraph make(File file) throws Exception {
        String fileContents = Files.readString(Paths.get(file.getPath()));
        StringTokenizer tokens = new StringTokenizer(fileContents, "\n");
        GridVertex start = parseVertex(tokens.nextToken());
        GridVertex goal = parseVertex(tokens.nextToken());
        StringTokenizer sizeTokens = new StringTokenizer(tokens.nextToken(), " ");
        int x = Integer.parseInt(sizeTokens.nextToken());
        int y = Integer.parseInt(sizeTokens.nextToken());
        GridGraph graph = new GridGraph(x+1, y+1);
        Grid gridReference = new Grid(x+1, y+1);

        while (tokens.hasMoreTokens()) {
            GridVertex v = parseVertex(tokens.nextToken());
            if (v.isBlocked()) {
                gridReference.blockVertex(v);
            }
        }
        setNeighborsForGridGraph(graph, gridReference);
        graph.setStartVertex(start.getXAxis(), start.getYAxis());
        graph.setGoalVertex(goal.getXAxis(), goal.getYAxis());
        return graph;
    }

    public GridGraph make(Grid grid) {
        GridGraph graph = new GridGraph(grid.getCols(), grid.getRows());
        setNeighborsForGridGraph(graph, grid);
        return graph;
    }

    private void setNeighborsForGridGraph(GridGraph graph, Grid reference) {
        for (int i = 0; i < graph.getNumRows(); i++) {
            for (int j = 0; j < graph.getNumCols(); j++) {
                addNeighborsForVertex(new GraphVertex(j, i), graph, reference, j, i);
                addNeighborsForVertex(new GraphVertex(j, i), graph, reference, j - 1, i);
                addNeighborsForVertex(new GraphVertex(j, i), graph, reference, j - 1, i - 1);
                addNeighborsForVertex(new GraphVertex(j, i), graph, reference, j, i - 1);
            }
        }
    }

    private void addNeighborsForVertex(GraphVertex v, GridGraph graph, Grid reference, int refX, int refY) {
        if (reference.isValid(refX, refY)) {
            GridVertex vRef = reference.getVertexAt(refX, refY);
            if (!vRef.isBlocked() && reference.isValid(refX + 1, refY + 1)) {
                graph.addNeighbor(v, new GraphVertex(vRef.getXAxis(), vRef.getYAxis()));
                graph.addNeighbor(v, new GraphVertex(vRef.getXAxis() + 1, vRef.getYAxis()));
                graph.addNeighbor(v, new GraphVertex(vRef.getXAxis(), vRef.getYAxis() + 1));
                graph.addNeighbor(v, new GraphVertex(vRef.getXAxis() + 1, vRef.getYAxis() + 1));
            }
        }
    }

    private GridVertex parseVertex(String str) {
        StringTokenizer tokens = new StringTokenizer(str, " ");
        return new GridVertex(Integer.parseInt(tokens.nextToken()) - 1, Integer.parseInt(tokens.nextToken()) - 1,
                tokens.hasMoreTokens() && Integer.parseInt(tokens.nextToken()) == 1);
    }
}
