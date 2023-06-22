package com.titan.gui;

import com.titan.model.LandingModule;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * class that represents a given CelestialObject on the screen
 */
public class ModuleGUI extends Parent {

    private final LandingModule module;
    private final Circle circle = new Circle();
    private final Line xAxis = new Line();
    private final Line yAxis = new Line();
    private final Text up = new Text("UP");
    private final Text down = new Text("DOWN");
    private final Text left = new Text("LEFT");
    private final Text right = new Text("RIGHT");


    /**
     * Constructs a GUI object based on a given LandingModule, adds the module and its label to the screen
     * @param module CelestialObject a GUI representation of which is being drawn
     */
    public ModuleGUI(LandingModule module) {
        this.module = module;

        circle.setStrokeWidth(2);
        circle.setRadius(40);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(module.getColor());
        circle.setCenterX(50);
        circle.setCenterY(50);

        xAxis.setStartX(0);
        xAxis.setStartY(50);
        xAxis.setEndX(100);
        xAxis.setEndY(50);

        yAxis.setStartX(50);
        yAxis.setStartY(0);
        yAxis.setEndX(50);
        yAxis.setEndY(100);

        xAxis.setStroke(Color.SILVER);
        yAxis.setStroke(Color.SILVER);

        up.setX(40);
        up.setY(5);
        up.setFill(Color.WHITE);

        down.setX(25);
        down.setY(110);
        down.setFill(Color.WHITE);

        left.setX(-35);
        left.setY(40);
        left.setFill(Color.WHITE);

        right.setX(95);
        right.setY(40);
        right.setFill(Color.WHITE);

        up.setStyle("-fx-font-size: 20px;");

        getChildren().addAll(circle, xAxis, yAxis, up, down, left, right);
    }

    /**
     * changes the visual appearance of the planets by switching between showing names and changing size depending on the variables
     */
    public void repaint() {
        setRotate(-module.getRotationAngle());
    }

    /**
     * updates the position of a celestial body on the screen
     */
    public void updatePosition() {

    }
}


