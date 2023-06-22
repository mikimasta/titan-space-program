package com.titan.gui;

import com.titan.math.Vector;
import com.titan.model.LandingModule;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

/**
 * class that represents a given CelestialObject on the screen
 */
public class LandingModuleGUI extends Parent {


    /**
     * determines if the names of the planets are being displayed on the screen
     */
    public static boolean displayNames = true;

    /**
     * determines whether the orbits are being drawn on the screen
     */
    public static boolean drawTail = true;

    /**
     * trail a given celestial object leaves behind
     */
    private Polyline tail = new Polyline();

    private final LandingModule module;
    private final Circle circle = new Circle();
    private final Text text = new Text();

    /**
     * Constructs a GUI object based on a given LandingModule, adds the module and its label to the screen
     * @param module CelestialObject a GUI representation of which is being drawn
     */
    public LandingModuleGUI(LandingModule module) {
        this.module = module;
        circle.setStrokeWidth(2);
        circle.setRadius(module.getRadius());
        circle.setFill(module.getColor());
        circle.setStroke(module.getColor());

        text.setText(module.getName());
        text.setStyle("-fx-font-size: 20px;");
        text.setFill(Color.SILVER);
        text.setX(5);
        text.setY(-5);

        getChildren().addAll(circle, text);
    }

    /**
     * changes the visual appearance of the planets by switching between showing names and changing size depending on the variables
     */
    public void repaint() {
        if (displayNames) {
            text.setText(module.getName());
        } else {
            text.setText("");
        }
        updatePosition();
        drawTail();
    }

    /**
     * getter method of the X coordinate of a given object on the screen
     * @return scaled pixel representation of the X coordinate
     */
    public int getCurrentX() {
        return (int) - (module.getX() / TitanLanding.scale);
    }

    /**
     * getter method of the Y coordinate of a given object on the screen
     * @return scaled pixel representation of the Y coordinate
     */
    public int getCurrentY() {
        return (int) - (module.getY() / TitanLanding.scale);
    }

    /**
     * Gets the underlying celestial-object.
     * @return the object
     */
    public LandingModule getModule() {
        return module;
    }

    /**
     * updates the position of a celestial body on the screen
     */
    public void updatePosition() {
        circle.setCenterX(getCurrentX() + TitanLanding.X_CENTER);
        circle.setCenterY(getCurrentY() + TitanLanding.Y_CENTER);

        text.setLayoutX(getCurrentX() + TitanLanding.X_CENTER);
        text.setLayoutY(getCurrentY() + TitanLanding.Y_CENTER);

        if (module.getHistoricPositions().size() > tail.getPoints().size() / 2) {
            drawTail();
        }

    }

    /**
     * draws the tail a given celestial object leaves behind
     */
    public void drawTail() {
        getChildren().remove(tail);
        tail = new Polyline();
        if (drawTail) {
            for (Vector v : module.getHistoricPositions()) {
                tail.getPoints().add((v.getValue(0) / TitanLanding.scale) + TitanLanding.X_CENTER);
                tail.getPoints().add((v.getValue(1) / TitanLanding.scale) + TitanLanding.Y_CENTER);
            }
            tail.getPoints().add(1.0 * getCurrentX() + TitanLanding.X_CENTER);
            tail.getPoints().add(1.0 * getCurrentY() + TitanLanding.Y_CENTER);

            tail.setFill(Color.TRANSPARENT);
            tail.setStroke(new Color(0.9, 0.9, 0.9, 0.5));
            getChildren().add(tail);
        }
    }
}


