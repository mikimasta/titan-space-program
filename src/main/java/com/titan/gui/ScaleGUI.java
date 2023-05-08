package com.titan.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * class that shows the scale in the lower right corner of the screen
 */
public class ScaleGUI extends Parent {

    private final Text text = new Text();
    private final Line line = new Line();
    private final Line lineStart = new Line();
    private final Line lineCenter = new Line();
    private final Line lineEnd = new Line();

    private final int length = 100;
    private final int margin = 50;

    /**
     * constructs a scaleGUI object
     */
    public ScaleGUI() {

        line.setStartX(Titan.WIDTH - (length + margin));
        line.setStartY(Titan.HEIGHT - margin);
        line.setEndX(Titan.WIDTH - margin);
        line.setEndY(Titan.HEIGHT - margin);
        line.setStroke(Color.SILVER);
        line.setStrokeWidth(2);

        lineStart.setStartX(Titan.WIDTH - (length + margin));
        lineStart.setStartY(Titan.HEIGHT - margin);
        lineStart.setEndX(Titan.WIDTH - (length + margin));
        lineStart.setEndY(Titan.HEIGHT - (margin + 10));
        lineStart.setStroke(Color.SILVER);
        lineStart.setStrokeWidth(2);

        lineCenter.setStartX(Titan.WIDTH - (length / 2 + margin));
        lineCenter.setStartY(Titan.HEIGHT - margin);
        lineCenter.setEndX(Titan.WIDTH - (length / 2 + margin));
        lineCenter.setEndY(Titan.HEIGHT - (margin + 5));
        lineCenter.setStroke(Color.SILVER);
        lineCenter.setStrokeWidth(2);

        lineEnd.setStartX(Titan.WIDTH - margin);
        lineEnd.setStartY(Titan.HEIGHT - margin);
        lineEnd.setEndX(Titan.WIDTH - margin);
        lineEnd.setEndY(Titan.HEIGHT - (margin + 10));
        lineEnd.setStroke(Color.SILVER);
        lineEnd.setStrokeWidth(2);

        text.setX(Titan.WIDTH - (length + margin));
        text.setY(Titan.HEIGHT - (margin + 20));
        updateScale();
        text.setFill(Color.WHITE);

        getChildren().add(text);
        getChildren().add(line);
        getChildren().add(lineStart);
        getChildren().add(lineCenter);
        getChildren().add(lineEnd);
    }

    /**
     * updates the scale value depending on mouse scroll
     */
    public void updateScale() {
        text.setText(Titan.scale * length + " km");
    }
}
