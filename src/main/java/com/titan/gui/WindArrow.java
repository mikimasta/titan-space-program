package com.titan.gui;


import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import com.titan.controls.Wind;

class WindArrow extends Parent {

    private Polygon arrowhead;
    private Line line;
    private Wind wind;
    private Text windSpeed;

    WindArrow(Wind wind) {
        this.wind = wind;
        
        windSpeed = new Text();
        windSpeed.setText("Speed: " + this.wind.getWindSpeed());

        line = new Line();
        line.setStartX(500);
        line.setEndX(600);
        line.setStartY(600);
        line.setEndY(600);
        line.setStroke(Color.SILVER);

        arrowhead = new Polygon();
        arrowhead.getPoints().addAll(new Double[]{
            500d, 600d,
            520d, 610d,
            520d, 590d
        });
        arrowhead.setFill(Color.SILVER);

        getChildren().addAll(line, arrowhead, windSpeed);
    }

    
    void update() {
        //setRotate(wind.getAngle());
    }
}


