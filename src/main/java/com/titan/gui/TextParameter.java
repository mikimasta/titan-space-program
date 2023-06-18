package com.titan.gui;

import com.titan.model.LandingModule;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

class TextParameter extends Text {

    enum ParameterType {
        TOTAL_VELOCITY,
        X_VELOCITY,
        Y_VELOCITY
    }


    private String text;
    private final ParameterType type;

    TextParameter(String infoToDisplay, ParameterType type) {
        text = infoToDisplay;
        this.type = type;
        this.setFill(Color.WHITE);
    }

    void update(LandingModule landingModule) {
        switch (this.type) {

            case TOTAL_VELOCITY:
                this.setText(text + landingModule.getVelocity().getLength() + " km/h");
                break;

            case X_VELOCITY:
                this.setText(text + landingModule.getVelocity().getValues()[0] + " km/h");
                break;

            case Y_VELOCITY:
                this.setText(text + landingModule.getVelocity().getValues()[1] +  " km/h");
                break;

        }
    }

}
