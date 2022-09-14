package com.visualizer.visualizationapp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class GridFileFactory {
    private int _rows = 50;
    private int _cols = 100;
    private final int _numOfFiles;
    private int _filesGenerated;
    private final String _directory;

    public GridFileFactory(String directory, int numOfFiles) {
        _directory = directory;
        _numOfFiles = numOfFiles;
    }

    public GridFileFactory(int rows, int cols, String directory, int numOfFiles) {
        _rows = rows;
        _cols = cols;
        _directory = directory;
        _numOfFiles = numOfFiles;
    }

    public void make() {
        for (_filesGenerated = 0; _filesGenerated < _numOfFiles;) {
            try {
                generateGridFile();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(e.getMessage());
            }
        }
    }

    private void generateGridFile() throws Exception {
        Grid grid = new Grid(_cols + 1, _rows + 1);
        int tenPercentOfCells = Math.round(_cols*_rows*0.1f);

        for (int y = 0; y < grid.getRows() - 1; y++) {
            ArrayList<Integer> randomNumberSequence = generateUniqueRandomNumberSequence(tenPercentOfCells);
            for (int i = 0; i < randomNumberSequence.size(); i++) {
                int x = randomNumberSequence.get(i);
                grid.getVertexAt(x, y).block();
            }
        }

        int startX = randomCol(), goalX = randomCol();
        int startY = randomRow(), goalY = randomRow();

        while ((startX == goalX && startY == goalY)) {
            startX = randomCol();
            goalX = randomCol();
            startY = randomRow();
            goalY = randomRow();
        }
        grid.setStartVertex(startX, startY);
        grid.setGoalVertex(goalX, goalY);
        GraphVertex startVertex = new GraphVertex(startX, startY);
        GraphVertex goalVertex = new GraphVertex(goalX, goalY);
        GridGraph graph = new GraphFactory().make(grid);

        if (graph.isReachableBFS(startVertex, goalVertex)) {
            writeFile(grid);
            _filesGenerated++;
        }
    }

    private ArrayList<Integer> generateUniqueRandomNumberSequence(int numOfCells) {
        ArrayList sequence = new ArrayList();
        ArrayList randomNumbers = new ArrayList(_cols);
        for (int i = 0; i < _cols; i++) {
            randomNumbers.add(i);
        }

        Collections.shuffle(randomNumbers);
        int count = numOfCells / _rows == 0 ? 1 : numOfCells / _rows;
        for (int i = 0; i < count; i++) {
            sequence.add(randomNumbers.get(i));
        }

        return sequence;
    }

    private int randomCol() {
        return (int)(Math.random() * _cols) + 1;
    }

    private int randomRow() {
        return (int)(Math.random() * _rows) + 1;
    }

    private void writeFile(Grid grid) throws Exception {
        ArrayList lines = gridToLines(grid);
        Files.write(Paths.get(_directory + "/test" + _filesGenerated + ".txt"), lines,
                StandardCharsets.UTF_8);
    }

    private ArrayList<String> gridToLines(Grid grid) {
        ArrayList lines = new ArrayList(grid.getRows() * grid.getCols());
        lines.add(grid.getStartVertex().getXAxis() + " " + grid.getStartVertex().getYAxis());
        lines.add(grid.getGoalVertex().getXAxis() + " " + grid.getGoalVertex().getYAxis());
        lines.add((grid.getCols() - 1) + " " + (grid.getRows() - 1));
        for (int i = 0; i < grid.getCols() - 1; i++) {
            for (int j = 0; j < grid.getRows() - 1; j++) {
                lines.add(grid.getVertexAt(i, j).toString());
            }
        }
        return lines;
    }
}
