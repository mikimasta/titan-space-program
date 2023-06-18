package com.titan.gui;


import javafx.scene.Scene;
import com.titan.LandingSimulation;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.model.LandingModule;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TitanLanding {

    public static double scale = 0.3;
    public final static int WIDTH = 1400;
    public final static int HEIGHT = 800;
    public static int xCenter = 700;
    public static int yCenter = 750;
    boolean running = false;

    private LandingSimulation simulation;

    private LandingModule landingModule;

    private LandingModuleGUI module;

    public TitanLanding() {
        
        landingModule = new LandingModule("Landing Module");
        module = new LandingModuleGUI(landingModule);
        simulation = new LandingSimulation(new RungeKuttaSolver(), 1, landingModule);

    }

    public Scene getScene()  {

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);


        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getStylesheets().add(("styling.css"));


        root.getChildren().add(module);
        XAxisGUI xAxis = new XAxisGUI();
        root.getChildren().add(xAxis);

        module.updatePosition();
        module.repaint();
        // System.out.println(module.getCurrentX());
        // System.out.println(module.getCurrentY());



        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                running = !running;
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, e -> {
            double delta = (scale / 10) * (e.getDeltaY() / 32);
            double newScale = scale - delta;
            if(newScale > 0.0001 && newScale < 2) scale = newScale;
            module.repaint();
            xAxis.updateScale();
        });

        
        KeyFrame kf = new KeyFrame(Duration.millis(5), e -> {
            if (running) {

                for (int i = 0; i < 1; i++) {
                    simulation.nextStep(i);
                    module.updatePosition();
                    module.repaint();
                }
            }
        });


        Timeline tl = new Timeline(kf);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        return scene;
    }


}
