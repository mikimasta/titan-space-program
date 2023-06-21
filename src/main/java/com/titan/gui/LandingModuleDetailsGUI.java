package com.titan.gui;

import com.titan.model.LandingModule;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * class that represents a given CelestialObject on the screen
 */
public class LandingModuleDetailsGUI extends Parent {

    private final LandingModule module;
    private final ModuleGUI moduleGUI;
    private final Circle circle = new Circle();
    private final Text xSpeed = new Text();
    private final Text ySpeed = new Text();
    private final Text totalSpeed = new Text();
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

        //xSpeed.setText(module.getName());
        xSpeed.setStyle("-fx-font-size: 20px;");
        xSpeed.setFill(Color.SILVER);
        setLayoutX(100);
        setLayoutY(200);
        moduleGUI = new ModuleGUI(module);

        getChildren().addAll(moduleGUI, xSpeed);
    }

    /**
     * changes the visual appearance of the planets by switching between showing names and changing size depending on the variables
     */
    public void repaint() {
        moduleGUI.repaint();
        updatePosition();
    }

    /**
     * updates the position of a celestial body on the screen
     */
    public void updatePosition() {

    }
}


