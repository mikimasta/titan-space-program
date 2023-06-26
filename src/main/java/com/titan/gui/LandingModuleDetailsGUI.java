package com.titan.gui;

import com.titan.model.LandingModule;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Locale;

/**
 * class that represents a given CelestialObject on the screen
 */
public class LandingModuleDetailsGUI extends Parent {

    private final LandingModule module;
    private final ModuleGUI moduleGUI;
    private final Circle circle = new Circle();
    private final Text height = new Text();
    private final Text totalSpeed = new Text();
    private final Text xSpeed = new Text();
    private final Text ySpeed = new Text();
    private final Text rotation = new Text();
    private final Parent model = new Group();

    /**
     * Constructs a GUI object based on a given LandingModule, adds the module and its label to the screen
     * @param module CelestialObject a GUI representation of which is being drawn
     */
    public LandingModuleDetailsGUI(LandingModule module) {
        this.module = module;

        circle.setStrokeWidth(2);
        circle.setRadius(20);
        circle.setFill(module.getColor());
        circle.setStroke(module.getColor());
        circle.setCenterX(50);
        circle.setCenterY(50);

        height.setStyle("-fx-font-size: 20px;");
        height.setFill(Color.SILVER);
        height.setY(140);

        rotation.setStyle("-fx-font-size: 20px;");
        rotation.setFill(Color.SILVER);
        rotation.setY(165);

        totalSpeed.setStyle("-fx-font-size: 20px;");
        totalSpeed.setFill(Color.SILVER);
        totalSpeed.setY(190);

        xSpeed.setStyle("-fx-font-size: 20px;");
        xSpeed.setFill(Color.SILVER);
        xSpeed.setY(235);

        ySpeed.setStyle("-fx-font-size: 20px;");
        ySpeed.setFill(Color.SILVER);
        ySpeed.setY(280);


        setLayoutX(50);
        setLayoutY(50);
        moduleGUI = new ModuleGUI(module);
        moduleGUI.setLayoutX(25);

        getChildren().addAll(moduleGUI, height, rotation, totalSpeed, xSpeed, ySpeed);
    }

    /**
     * changes the visual appearance of the planets by switching between showing names and changing size depending on the variables
     */
    public void repaint() {
        moduleGUI.repaint();
        height.setText(String.format(Locale.ENGLISH, "Height: %.5f km", module.getY()));
        rotation.setText(String.format(Locale.ENGLISH, "Rotation: %.2f°", -module.getRotationAngle()));

        totalSpeed.setText(String.format(Locale.ENGLISH, "Total speed: %.5f km/s\n(%d km/h))",
                module.getTotalSpeed(),
                Math.round(module.getTotalSpeed() * 3600)));

        xSpeed.setText(String.format(Locale.ENGLISH, "X-Velocity: %.5f km/s\n(%d km/h))",
                module.getVelocity().getValue(0),
                Math.abs(Math.round(module.getVelocity().getValue(0) * 3600))));

        ySpeed.setText(String.format(Locale.ENGLISH, "Y-Velocity: %.5f km/s\n(%d km/h))",
                module.getVelocity().getValue(1),
                Math.abs(Math.round(module.getVelocity().getValue(1) * 3600))));

        updatePosition();
    }

    /**
     * updates the position of a celestial body on the screen
     */
    public void updatePosition() {

    }
}


