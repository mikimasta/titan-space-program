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
    private static final Image background = new Image("/background.jpg");
    public static final BackgroundImage BACKGROUND_IMAGE = new BackgroundImage(background,
             BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

    /**
     * window icon
     */
    public static final Image icon = new Image("/icon.png");

    public static final Image icon300 = new Image("/icon300.png");
}
