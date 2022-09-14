package com.visualizer.visualizationapp;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GridMaker {
    private final File _folder;
    private final Stage _stage;
    private final Popup _gridPopup;
    private final Group _group;

    public GridMaker(String directory, Stage stage)
    {
        _folder = new File(directory);
        _stage = stage;
        _gridPopup = new Popup();
        _group = new Group();
    }

    public Group make() {
        GridPane outerGrid = new GridPane();
        outerGrid.setVgap(20);
        outerGrid.setPadding(new Insets(20, 20, 20, 20));
        Benchmarker b1 = new Benchmarker();
        Benchmarker b2 = new Benchmarker();
        try {
            int row = 0;
            for (File file : _folder.listFiles()) {
                GridGraph graph = new GraphFactory().make(file);

                b1.start();
                ArrayList<GraphVertex> path = new PathFinder(graph).aStarPath(false);
                b1.stop();

                b2.start();
                ArrayList<GraphVertex> path2 = new PathFinder(graph).aStarPath(true);
                b2.stop();

                Pane grid = createGridPathPlanningVisualization(file, path, path2);
                if (row == 0) {
                    Pane visGrid = createVisibilityGraph(file, graph);
                    GridPane firstGrid = new GridPane();
                    firstGrid.getChildren().add(visGrid);
                    GridPane.setRowIndex(grid, 1);
                    firstGrid.getChildren().add(grid);
                    grid = firstGrid;
                }

                GridPane.setRowIndex(grid, row++);
                outerGrid.getChildren().add(grid);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Not valid file input!");
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(outerGrid);
        scrollPane.setPrefSize(1200, 700);
        System.out.println("A* time: " + b1.averageTimeInNanos());
        System.out.println("Theta* time: " + b2.averageTimeInNanos());
        VBox vbox = new VBox();
        vbox.getChildren().add(scrollPane);
        String aAvg = "A* avg time: " + b1.averageTimeInNanos() / 1000000.0 + "ms";
        String thetaAvg = "Theta* avg time: " + b2.averageTimeInNanos() / 1000000.0 + "ms";
        Label timestamp = new Label(aAvg + "\n" + thetaAvg);
        timestamp.setPadding(new Insets(20));
        vbox.getChildren().add(timestamp);
        _group.getChildren().add(vbox);
        return _group;
    }

    private BorderPane createGridPathPlanningVisualization(File file, ArrayList<GraphVertex> path, ArrayList<GraphVertex> path2) throws Exception {
        BorderPane gridOverlay = new BorderPane();
        Pane grid = createGridFromFile(file);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(grid);
        drawPath(stackPane, path);
        drawPath(stackPane, path2);
        //drawStraightLinePath(stackPane, path);
        gridOverlay.setCenter(stackPane);
        gridOverlay.setTop(createGridTitle(file, path, path2));
        return gridOverlay;
    }

    private BorderPane createVisibilityGraph(File file, GridGraph graph) throws Exception {
        BorderPane gridOverlay = new BorderPane();
        Pane grid = createGridFromFile(file);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(grid);
        ArrayList<VertexInfo> visGraph = new PathFinder(graph).generateVisibilityGraph();
        drawVisibilityPaths(stackPane, visGraph);
        gridOverlay.setCenter(stackPane);
        return gridOverlay;
    }

    private GridPane createGridFromFile(File file) throws Exception {
        GridPane grid = new GridPane();
        String fileContents = Files.readString(Paths.get(file.getPath()));
        StringTokenizer tokens = new StringTokenizer(fileContents, "\n");
        GridVertex startVertex = parseVertex(tokens.nextToken());
        GridVertex endVertex = parseVertex(tokens.nextToken());
        StringTokenizer sizeTokens = new StringTokenizer(tokens.nextToken(), " ");
        int gridWidth = Integer.parseInt(sizeTokens.nextToken());
        int gridHeight = Integer.parseInt(sizeTokens.nextToken());
        while (tokens.hasMoreTokens()) {
            GridVertex v = parseVertex(tokens.nextToken());
            Rectangle rec = setRectangle(v);
            grid.getChildren().add(rec);
        }
        return grid;
    }

    private Label createGridTitle(File file, ArrayList<GraphVertex> path, ArrayList<GraphVertex> path2) {
        String title = file.getName() + "   A* Path Distance = " + calculatePathDistance(path);
        title += "   Theta* Path Distance = " + calculatePathDistance(path2);
        return new Label(title);
    }

    private void drawPath(StackPane stackPane, ArrayList<GraphVertex> path) {
        for (int i = 0; i < path.size(); i++) {
            GraphVertex v = path.get(i);
            if (i < path.size() - 1)
                drawLineSegment(v, path.get(i+1), stackPane);

            if (i == 0) {
                setVertex(v, stackPane, Color.RED);
            } else if (i + 1 == path.size()) {
                setVertex(v, stackPane, Color.GREEN);
            } else {
                setVertex(v, stackPane, Color.BLUE);
            }
        }
    }

    private void drawVisibilityPaths(StackPane stackPane, ArrayList<VertexInfo> visGraph) {
        for (int i = 0; i < visGraph.size(); i++) {
            VertexInfo vertex = visGraph.get(i);
            ArrayList<GraphVertex> lineOfSightVertices = vertex.getNeighbors();

            for (int j = 0; j < lineOfSightVertices.size(); j++) {
                drawVisibilityLineSegment(vertex.getVertex(), lineOfSightVertices.get(j), stackPane);
            }

            if (i == 0) {
                setVertex(vertex.getVertex(), stackPane, Color.GREEN);
            } else if ( i == 1) {
                setVertex(vertex.getVertex(), stackPane, Color.RED);
            } else {
                setVertex(vertex.getVertex(), stackPane, Color.BLUE);
            }
        }
    }

    private void drawStraightLinePath(StackPane stackPane, ArrayList<GraphVertex> path) {
        if (path.size() < 2) {
            return;
        }

        GraphVertex goal = path.get(0);
        GraphVertex start = path.get(path.size() - 1);
        drawLineSegment(goal, start, stackPane);
        setVertex(goal, stackPane, Color.RED);
        setVertex(start, stackPane, Color.GREEN);
    }

    private void drawLineSegment(GraphVertex v1, GraphVertex v2, StackPane stackPane) {
        Line line = new Line();
        if (v1.calculateDistance(v2) >= 2) {
            line.setStroke(Color.ORANGE);
        } else {
            line.setStroke(Color.MEDIUMTURQUOISE);
        }

        line.setStrokeWidth(2);
        stackPane.getChildren().add(line);
        StackPane.setAlignment(line, Pos.TOP_LEFT);
        line.setStartX(v1.getXAxis()*10 + v1.getXAxis());
        line.setStartY(v1.getYAxis()*10 + v1.getYAxis());
        line.setEndX(v2.getXAxis()*10 + v2.getXAxis());
        line.setEndY(v2.getYAxis()*10 + v2.getYAxis());
        int centerX = (Math.min(v1.getXAxis(), v2.getXAxis()))*11 - 1;
        int centerY = (Math.min(v1.getYAxis(), v2.getYAxis()))*11 - 1;
        StackPane.setMargin(line, new Insets(centerY,0,0, centerX));
    }

    private void drawVisibilityLineSegment(GraphVertex v1, GraphVertex v2, StackPane stackPane) {
        Line line = new Line();
        line.setStroke(Color.ORANGE);
        line.setOpacity(0.2);
        line.setStrokeWidth(1);
        stackPane.getChildren().add(line);
        StackPane.setAlignment(line, Pos.TOP_LEFT);
        line.setStartX(v1.getXAxis()*10 + v1.getXAxis());
        line.setStartY(v1.getYAxis()*10 + v1.getYAxis());
        line.setEndX(v2.getXAxis()*10 + v2.getXAxis());
        line.setEndY(v2.getYAxis()*10 + v2.getYAxis());
        int centerX = (Math.min(v1.getXAxis(), v2.getXAxis()))*11 - 1;
        int centerY = (Math.min(v1.getYAxis(), v2.getYAxis()))*11 - 1;
        StackPane.setMargin(line, new Insets(centerY,0,0, centerX));
    }

    private GridVertex parseVertex(String vertexString) {
        StringTokenizer tokens = new StringTokenizer(vertexString, " ");
        if (tokens.countTokens() == 3) {
            return new GridVertex(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()) == 1);
        } else {
            return new GridVertex(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), false);
        }
    }

    private Rectangle setRectangle(GridVertex v) {
        Rectangle rec = new Rectangle();
        rec.setWidth(10);
        rec.setHeight(10);
        rec.setFill(setRectangleFill(v));
        rec.setStroke(Color.BLACK);
        GridPane.setRowIndex(rec, v.getYAxis());
        GridPane.setColumnIndex(rec, v.getXAxis());
        return rec;
    }

    private Color setRectangleFill(GridVertex v) {
        return v.isBlocked() ? Color.GREY : Color.WHITE;
    }

    private void setVertex(GraphVertex v, StackPane stackPane, Paint color) {
        GridCircle c = new GridCircle(v);
        c.setRadius(3);
        c.setFill(color);
        stackPane.getChildren().add(c);
        StackPane.setAlignment(c, Pos.TOP_LEFT);
        StackPane.setMargin(c, new Insets(v.getYAxis()*10 + v.getYAxis() - c.getRadius(), 0, 0, v.getXAxis()*10 + v.getXAxis() - c.getRadius()));
        setMouseClickEvent(c);
    }

    private void setMouseClickEvent(GridCircle c) {
        c.setOnMouseClicked(clickVertexEvent(c));
    }

    private EventHandler<MouseEvent> clickVertexEvent(GridCircle c) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                _gridPopup.show(_stage, (_stage.getX() + 5), (_stage.getY() + 30));
                _gridPopup.getContent().clear();
                Label label = new Label(c.toString());
                label.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                _gridPopup.getContent().add(label);
            }
        };
    }

    private double calculatePathDistance(ArrayList<GraphVertex> path) {
        double pathDistance = 0;
        for (int i = 1; i < path.size(); i++) {
            GraphVertex curr = path.get(i);
            GraphVertex prev = path.get(i - 1);
            pathDistance += curr.calculateDistance(prev);
        }

        return Math.round(pathDistance * 1000) / 1000.0;
    }
}
