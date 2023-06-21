package com.titan.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * class that shows the scale in the lower right corner of the screen
 */
public class XAxisGUI extends Parent {

    private final Text text1 = new Text();
    private final Text text2 = new Text();

    private final int length = TitanLanding.WIDTH - 120;
    private final double numberOfSteps = 12;

    /**
     * constructs a scaleGUI object
     */
    public XAxisGUI() {

        Line line = new Line();
        int margin = 60;
        line.setStartX(TitanLanding.WIDTH - (length + margin));
        line.setStartY(TitanLanding.Y_CENTER);
        line.setEndX(TitanLanding.WIDTH - margin);
        line.setEndY(TitanLanding.Y_CENTER);
        line.setStroke(Color.SILVER);
        line.setStrokeWidth(2);

        Line lineStart = new Line();
        lineStart.setStartX(TitanLanding.WIDTH - (length + margin));
        lineStart.setStartY(TitanLanding.Y_CENTER);
        lineStart.setEndX(TitanLanding.WIDTH - (length + margin));
        lineStart.setEndY(TitanLanding.Y_CENTER - 10);
        lineStart.setStroke(Color.SILVER);
        lineStart.setStrokeWidth(2);

        for (int i = 1; i < numberOfSteps; i++) {
            Line lineCenter = new Line();
            lineCenter.setStartX(TitanLanding.WIDTH - (length / numberOfSteps)*i - margin);
            lineCenter.setStartY(TitanLanding.Y_CENTER);
            lineCenter.setEndX(TitanLanding.WIDTH - (length / numberOfSteps)*i - margin);
            if (numberOfSteps % 2 == 0 && numberOfSteps / 2 == i) {
                lineCenter.setEndY(TitanLanding.Y_CENTER - 10);
            } else {
                lineCenter.setEndY(TitanLanding.Y_CENTER - 5);
            }
            lineCenter.setStroke(Color.SILVER);
            lineCenter.setStrokeWidth(2);
            
            getChildren().add(lineCenter);
        }

        Line lineEnd = new Line();
        lineEnd.setStartX(TitanLanding.WIDTH - margin);
        lineEnd.setStartY(TitanLanding.Y_CENTER);
        lineEnd.setEndX(TitanLanding.WIDTH - margin);
        lineEnd.setEndY(TitanLanding.Y_CENTER - 10);
        lineEnd.setStroke(Color.SILVER);
        lineEnd.setStrokeWidth(2);

        text1.setX(TitanLanding.WIDTH - (length + margin));
        text1.setY(TitanLanding.HEIGHT - (margin + 25));
        text1.setFill(Color.WHITE);

        text2.setX(TitanLanding.WIDTH - (length + margin - 10));
        text2.setY(TitanLanding.HEIGHT - (margin + 8));
        text2.setFill(Color.WHITE);

        updateScale();

        getChildren().add(text1);
        getChildren().add(text2);
        getChildren().add(line);
        getChildren().add(lineStart);
        getChildren().add(lineEnd);
    }

    /**
     * updates the scale value depending on mouse scroll
     */
    public void updateScale() {
        text1.setText(String.format("%.3f km", TitanLanding.scale * length));
        text2.setText(String.format("%.3f km", TitanLanding.scale * length / numberOfSteps));
    }
}
