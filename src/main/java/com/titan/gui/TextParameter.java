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
                double velocity = landingModule.getVelocity().getLength();
                this.setText(text + velocity + " km/s\n" + "(" + Math.round(velocity * 3600) + " km/h)");
                break;

            case X_VELOCITY:
                double xVelocity = landingModule.getVelocity().getValues()[0];
                this.setText(text + xVelocity + " km/s\n" + "(" + Math.round(xVelocity * 3600) + " km/h)");
                break;

            case Y_VELOCITY:
                double yVelocity = landingModule.getVelocity().getValues()[1];
                this.setText(text + yVelocity + " km/s\n" + "(" + Math.round(yVelocity * 3600) + " km/h)");
                break;

        }
    }

}
