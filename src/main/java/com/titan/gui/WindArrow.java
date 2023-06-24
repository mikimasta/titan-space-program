package com.titan.gui;


import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;


class WindArrow extends Parent {

    private Polygon arrowhead;
    private Line line;

    WindArrow() {

        line = new Line();
        line.setStartX(0);
        line.setEndX(150);
        line.setStroke(Color.SILVER);
        line.setStrokeWidth(2d);

        arrowhead = new Polygon();
        arrowhead.getPoints().addAll(new Double[]{
            0d, 0d,
            20d, -10d,
            20d, 10d
        });
        arrowhead.setFill(Color.WHITE);


        setLayoutX(50);
        setLayoutY(500);
        getChildren().addAll(line, arrowhead);

    }
}


