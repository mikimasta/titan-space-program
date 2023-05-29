package com.titan.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class that displays the date
 */
public class DateGUI extends Parent {

    private final Text text = new Text();

    /**
     * constructs a DateGUI object
     */
    public DateGUI() {
        getChildren().add(text);
        text.setX(10);
        text.setY(20);
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 20px");
    }

    /**
     * updates the date depending on the current step
     */
    public void update(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = new Date(1680300000000L + (long) Titan.stepSize * Titan.currentStep * 1000);


        String dateString = "Current date: " + simpleDateFormat.format(date);


        text.setText(dateString);
    }
}
