package com.titan.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.net.URL;
/**
 * abstract class containing images
 */
public abstract class Images {

    /**
     * background image for the GUI
     */
    static URL background = Images.class.getResource("../../../../assets/background.jpg");
    // public static final BackgroundImage backgroundImage = new BackgroundImage(new Image(background.toString()),
    //         BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
}
