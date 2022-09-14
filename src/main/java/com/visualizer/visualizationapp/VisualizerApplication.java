package com.visualizer.visualizationapp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VisualizerApplication extends Application {
    private final int SCREEN_WIDTH = 1200;
    private final int SCREEN_HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        Parameters parameters = getParameters();
        String filePath = parameters.getUnnamed().get(0);
        GridMaker gridMaker = new GridMaker(filePath, stage);
        Scene scene = new Scene(gridMaker.make(), SCREEN_WIDTH,  SCREEN_HEIGHT);
        stage.setTitle("Path Planning Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String filePath = args[0];
            launch(filePath);
        } else {
            System.out.println("You must provide 1 argument that is a directory path!");
        }
    }
}