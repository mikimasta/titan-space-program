package com.titan.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * abstract class containing images
 */
public abstract class Images {

    /**
     * background image for the GUI
     */
    static Image background = new Image("/background.jpg");
    public static final BackgroundImage backgroundImage = new BackgroundImage(background,
             BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

    public static final Image icon = new Image("/icon.png");

    public static final Image icon300 = new Image("/icon300.png");
}
