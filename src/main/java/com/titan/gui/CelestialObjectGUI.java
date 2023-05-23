package com.titan.gui;

import com.titan.math.Vector;
import com.titan.model.CelestialObject;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

/**
 * class that represents a given CelestialObject on the screen
 */
public class CelestialObjectGUI extends Parent {

    /**
     * determines the size of the planets, 0 means they have sizes that are in relation to the actual size,
     * but they are always visible, 1 means they are the actual sizes and 2 means they are all the same size
     */
    public static int sizeMode = 0;

    /**
     * determines if the names of the planets are being displayed on the screen
     */
    public static boolean displayNames = true;

    /**
     * determines whether the orbits are being drawn on the screen
     */
    public static boolean drawOrbits = true;

    /**
     * trail a given celestial object leaves behind
     */
    private Polyline tail = new Polyline();

    private final CelestialObject object;
    private final Circle circle = new Circle();
    private final Text text = new Text();

    /**
     * Constructs a GUI object based on a given CelestialObject, adds the object and its label to the screen
     * @param object CelestialObject a GUI representation of which is being drawn
     */
    public CelestialObjectGUI(CelestialObject object) {
        this.object = object;
        circle.setStrokeWidth(1);
        circle.setRadius(object.getRadius());
        circle.setFill(object.getColor());
        circle.setStroke(object.getColor());

        text.setText(object.getName());
        text.setStyle("-fx-font-size: 20px;");
        text.setFill(Color.SILVER);

        getChildren().addAll(circle, text);
    }

    /**
     * changes the visual appearance of the planets by switching between showing names and changing size depending on the variables
     */
    public void repaint() {
        if (sizeMode == 0) {
            circle.setRadius(object.getRadius());
        } else if (sizeMode == 1) {
            circle.setRadius((int) ((object.getDiameter() / 2) / Titan.scale));
        } else {
            circle.setRadius(5);
        }

        if (displayNames) {
            text.setText(object.getName());
        } else {
            text.setText("");
        }
        drawTail();
    }

    /**
     * getter method of the X coordinate of a given object on the screen
     * @return scaled pixel representation of the X coordinate
     */
    public int getCurrentX() {
        return (int) (object.getPosition().getValues()[0] / Titan.scale);
    }

    /**
     * getter method of the Y coordinate of a given object on the screen
     * @return scaled pixel representation of the Y coordinate
     */
    public int getCurrentY() {
        return (int) (object.getPosition().getValues()[1] / Titan.scale);
    }

    /**
     * Gets the underlying celestial-object.
     * @return the object
     */
    public CelestialObject getObject() {
        return object;
    }

    /**
     * updates the position of a celestial body on the screen
     */
    public void updatePosition() {
        circle.setCenterX(getCurrentX() + Titan.xCenter);
        circle.setCenterY(getCurrentY() + Titan.yCenter);

        text.setLayoutX(getCurrentX() + Titan.xCenter);
        text.setLayoutY(getCurrentY() + Titan.yCenter);

        if (object.getHistoricPositions().size() > tail.getPoints().size() / 2) {
            drawTail();
        }

    }

    /**
     * draws the tail a given celestial object leaves behind
     */
    public void drawTail() {
        getChildren().remove(tail);
        tail = new Polyline();
        if (drawOrbits) {
            for (Vector v : object.getHistoricPositions()) {
                tail.getPoints().add((v.getValue(0) / Titan.scale) + Titan.xCenter);
                tail.getPoints().add((v.getValue(1) / Titan.scale) + Titan.yCenter);
            }
            tail.getPoints().add(1.0 * getCurrentX() + Titan.xCenter);
            tail.getPoints().add(1.0 * getCurrentY() + Titan.yCenter);

            tail.setFill(Color.TRANSPARENT);
            tail.setStroke(new Color(0.9, 0.9, 0.9, 0.5));
            getChildren().add(tail);
        }
    }
}


