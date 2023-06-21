package com.titan.gui;

import com.titan.Simulation;
import com.titan.controls.Controls;
import com.titan.controls.FlightControlsTwoEngineFiresForLaunch;
import com.titan.controls.Logger;
import com.titan.math.solver.AdamsBashforth2ndOrderSolver;
import com.titan.math.solver.EulerSolver;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.math.solver.Solver;
import com.titan.model.CelestialObject;
import com.titan.model.Rocket;
import com.titan.model.SolarSystem;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Titan extends Application {

    /**
     * scale we use to represent the solar system on the screen
     */
    public static double scale = 3500000;
    public final static int WIDTH = 1400;
    public final static int HEIGHT = 800;

    private static String defaultButtonStyle = "-fx-font-size: 15px";;

    public static int xCenter = WIDTH / 2;
    public static int yCenter = HEIGHT / 2;

    private static double mouseX = 0;
    private static double mouseY = 0;

    public static int xCenter_ = WIDTH / 2;
    public static int yCenter_ = HEIGHT / 2;

    private String lockedInObject = "Sun";

    private boolean atTitan = false;

    /**
     * determines if the animation should be running or not
     */
    public static boolean running = false;


    public static int steps = 87600; // 10 years: every hour

    /**
     * stepSize for an Euler solver
     */
    public static int stepSize = 60; // 60 = 1 minute

    /**
     * value of the current step
     */
    public static int currentStep = 0;

    /**
     * determines how many steps at once we are calculating, more means faster animation
     */
    public static int stepsAtOnce = 50;

    static Stage gameWindow;


    @Override
    public void start(Stage stage) {
        gameWindow = stage;
        gameWindow.setTitle("Titan Space Program");
        gameWindow.setWidth(WIDTH);
        gameWindow.getIcons().add(Images.icon300);
        gameWindow.setHeight(HEIGHT);


        Pane root = new Pane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setBackground(new Background(Images.BACKGROUND_IMAGE));
        root.getStylesheets().add(("styling.css"));


        //Hide names button
        Button hideNames = new Button("hide/show names");
        hideNames.setStyle(defaultButtonStyle);
        hideNames.setLayoutX(WIDTH - 200);
        hideNames.setLayoutY(20);
        hideNames.setFocusTraversable(false);
        root.getChildren().add(hideNames);


        //resize objects button
        Button resizePlanets = new Button("resize planets (prop.)");
        resizePlanets.setStyle(defaultButtonStyle);
        resizePlanets.setLayoutX(WIDTH - 200);
        resizePlanets.setLayoutY(60);
        resizePlanets.setFocusTraversable(false);
        root.getChildren().add(resizePlanets);

        //back to center button
        Button backToCenter = new Button("back to center");
        backToCenter.setStyle(defaultButtonStyle);
        backToCenter.setLayoutX(WIDTH - 200);
        backToCenter.setLayoutY(100);
        backToCenter.setFocusTraversable(false);
        root.getChildren().add(backToCenter);

        //draw orbits button
        Button drawOrbits = new Button("draw orbits");
        drawOrbits.setStyle(defaultButtonStyle);
        drawOrbits.setLayoutX(WIDTH - 200);
        drawOrbits.setLayoutY(140);
        drawOrbits.setFocusTraversable(false);
        root.getChildren().add(drawOrbits);

        //center to titan button
        Button centerTitan = new Button("center on titan");
        centerTitan.setStyle(defaultButtonStyle);
        centerTitan.setLayoutX(WIDTH - 200);
        centerTitan.setLayoutY(180);
        centerTitan.setFocusTraversable(false);
        root.getChildren().add(centerTitan);

        //center to earth button
        Button centerEarth = new Button("center on earth");
        centerEarth.setStyle(defaultButtonStyle);
        centerEarth.setLayoutX(WIDTH - 200);
        centerEarth.setLayoutY(220);
        centerEarth.setFocusTraversable(false);
        root.getChildren().add(centerEarth);

        //center to rocket button
        Button centerRocket = new Button("center on rocket");
        centerRocket.setStyle(defaultButtonStyle);
        centerRocket.setLayoutX(WIDTH - 200);
        centerRocket.setLayoutY(260);
        centerRocket.setFocusTraversable(false);
        root.getChildren().add(centerRocket);

        Button landingButton = new Button("Attempt landing");
        landingButton.setStyle(defaultButtonStyle);
        landingButton.setLayoutX(WIDTH / 2 - 100);
        landingButton.setLayoutY(HEIGHT - 80);
        landingButton.setFocusTraversable(false);

        landingButton.setOnAction(e -> {
            
            TitanLanding landing = new TitanLanding();

            landing.setPreviousScene(scene);

            gameWindow.setScene(landing.getScene());

            root.getChildren().remove(landingButton);
        });


        DateGUI date = new DateGUI();
        root.getChildren().add(date);

        ScaleGUI scaleGUI = new ScaleGUI();
        root.getChildren().add(scaleGUI);


        // will display mission mlog 
        Button missionLog = new Button("Mission Log");
        missionLog.setStyle(defaultButtonStyle);
        missionLog.setLayoutX(10);
        missionLog.setLayoutY(25);
        missionLog.setFocusTraversable(false);
        root.getChildren().add(missionLog);

        TextArea mlog = new TextArea();
        mlog.setLayoutX(10);
        mlog.setLayoutY(50);
        mlog.setWrapText(true);
        mlog.setMaxHeight(90);
        mlog.setMaxWidth(400);
        mlog.setEditable(false);
        mlog.setFocusTraversable(false);

        missionLog.setOnAction(e -> {

            root.requestFocus();
            if (!root.getChildren().contains(mlog)) root.getChildren().add(mlog);
            else root.getChildren().remove(mlog);


        });

        // will display engine mlog
        Button engineLog = new Button("Engine Log");
        engineLog.setStyle(defaultButtonStyle);
        engineLog.setLayoutX(20);
        engineLog.setLayoutY(HEIGHT - 80);
        engineLog.setFocusTraversable(false);
        root.getChildren().add(engineLog);
        
        TextArea elog = new TextArea();
        elog.setLayoutX(20);
        elog.setLayoutY(HEIGHT - 180);
        elog.setWrapText(true);
        elog.setMaxHeight(90);
        elog.setMaxWidth(400);
        elog.setEditable(false);
        elog.setFocusTraversable(false);

        engineLog.setOnAction(e -> {
            
            root.requestFocus();
            if (!root.getChildren().contains(elog)) root.getChildren().add(elog);
            else root.getChildren().remove(elog);
        });

        //SolarSystem system2 = new SolarSystem();
        SolarSystem system = new SolarSystem("/initial_conditions.csv");

        Rocket rocket = system.createRocketOnEarth("Rocket", 50000);
        system.stageRocket(rocket);

        ArrayList<CelestialObjectGUI> objects = new ArrayList<>();
        for(CelestialObject o : system.getCelestialObjects()) {
            CelestialObjectGUI objectGUI = new CelestialObjectGUI(o);
            objects.add(objectGUI);
            root.getChildren().add(objectGUI);
            objectGUI.updatePosition();
        }

        //Solver eulerSolver = new EulerSolver();
        Solver rungeKuttaSolver = new RungeKuttaSolver();
        //Solver adamsBashforth2 = new AdamsBashforth2ndOrderSolver();

        Controls controls3 = new FlightControlsTwoEngineFiresForLaunch();

        Simulation simulation = new Simulation(rungeKuttaSolver, stepSize, controls3, system, rocket);

        Logger missionLogger = controls3.getMissionLogger();
        Logger engineLogger = controls3.getEngineLogger();


        KeyFrame kf = new KeyFrame(Duration.millis(1), e -> {
            if (running) {

                mlog.setText(missionLogger.getLog());
                mlog.setScrollTop(Double.MAX_VALUE);
                elog.setText(engineLogger.getLog());
                elog.setScrollTop(Double.MAX_VALUE);

                for (int i = 0; i < stepsAtOnce; i++) {
                    simulation.nextStep(currentStep);
                    currentStep++;
                    if (Titan.currentStep == 365 * 24 * 60 + 1 || Titan.currentStep == 365 * 24 * 60 * 2 + 1 ) {
                        if (!atTitan)
                            root.getChildren().add(landingButton);
                        running = false;
                        atTitan = true;
                        // stepsAtOnce = 1;
                        break;
                    }
                }
            }
    

            centerOnObject(system, objects);

            for (CelestialObjectGUI o : objects) {
                o.updatePosition();
            }
            date.update();
        });


       hideNames.setOnAction(e -> {
           CelestialObjectGUI.displayNames = !CelestialObjectGUI.displayNames;
           for (CelestialObjectGUI o :objects) {
               o.repaint();
           }
       });

       resizePlanets.setOnAction(e -> {
           CelestialObjectGUI.sizeMode++;
           if (CelestialObjectGUI.sizeMode > 2) CelestialObjectGUI.sizeMode = 0;

           if (CelestialObjectGUI.sizeMode == 0) resizePlanets.setText("resize planets (prop.)");
           else if (CelestialObjectGUI.sizeMode == 1) resizePlanets.setText("resize planets (actual)");
           else resizePlanets.setText("resize planets (similar)");

           for (CelestialObjectGUI o :objects) {
               o.repaint();
           }
       });

       backToCenter.setOnAction(e -> {
           xCenter = WIDTH / 2;
           yCenter = HEIGHT / 2;
           lockedInObject = "Sun";
           for (CelestialObjectGUI o :objects) {
               o.repaint();
           }
       });

       drawOrbits.setOnAction(e -> {
           CelestialObjectGUI.drawOrbits = !CelestialObjectGUI.drawOrbits;
           for (CelestialObjectGUI o :objects) {
               o.repaint();
           }
       });

       centerTitan.setOnAction(e -> {
           xCenter = WIDTH / 2 - objects.get(system.getIndexTitan()).getCurrentX();
           yCenter = HEIGHT / 2 - objects.get(system.getIndexTitan()).getCurrentY();
           lockedInObject = "Titan";
           for (CelestialObjectGUI o :objects) {
               o.repaint();
           }
       });

        centerEarth.setOnAction(e -> {
            if (objects.get(3).getObject().getName().equals("Earth")) {
                xCenter = WIDTH / 2 - objects.get(3).getCurrentX();
                yCenter = HEIGHT / 2 - objects.get(3).getCurrentY();
                lockedInObject = "Earth";
                for (CelestialObjectGUI o :objects) {
                    o.repaint();
                }
            }
        });

        centerRocket.setOnAction(e -> {
            if (objects.get(objects.size()-1).getObject() instanceof Rocket) {
                xCenter = WIDTH / 2 - objects.get(objects.size()-1).getCurrentX();
                yCenter = HEIGHT / 2 - objects.get(objects.size()-1).getCurrentY();
                lockedInObject = "Rocket";
                for (CelestialObjectGUI o :objects) {
                    o.repaint();
                }
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, e -> {
            double delta = (e.getDeltaY() * (scale / 500.0));
            if (delta < 0 && delta > -2) delta = -2;
            if (delta > 0 && delta <  2) delta = 2;
            double newScale = scale - delta;
            if(newScale > 5) scale = (int) newScale;
            scaleGUI.updateScale();

            centerOnObject(system, objects);

            for (CelestialObjectGUI o : objects) {
                o.repaint();
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                running = !running;
            }
        });

        root.setOnMousePressed(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
            xCenter_ = xCenter;
            yCenter_ = yCenter;
        });


        root.setOnMouseDragged(e-> {
            xCenter = (int) (xCenter_ + e.getSceneX() - mouseX);
            yCenter = (int) (yCenter_ + e.getSceneY() - mouseY);
            lockedInObject = "Sun";
            for (CelestialObjectGUI o : objects) {
                o.drawTail();
            }
        });

        root.setOnMouseReleased(e -> {
            xCenter_ = xCenter;
            yCenter_ = yCenter;
        });

        Timeline tl = new Timeline(kf);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        hideNames.toFront();
        resizePlanets.toFront();
        backToCenter.toFront();
        drawOrbits.toFront();
        centerTitan.toFront();
        mlog.toFront();
        missionLog.toFront();
        engineLog.toFront();

        gameWindow.setScene(scene);
        gameWindow.show();
    }

    private void centerOnObject(SolarSystem system, ArrayList<CelestialObjectGUI> objects) {
        if(lockedInObject.equals("Titan")) {
            xCenter = WIDTH / 2 - objects.get(system.getIndexTitan()).getCurrentX();
            yCenter = HEIGHT / 2 - objects.get(system.getIndexTitan()).getCurrentY();
        } else if(lockedInObject.equals("Earth")) {
            xCenter = WIDTH / 2 - objects.get(3).getCurrentX();
            yCenter = HEIGHT / 2 - objects.get(3).getCurrentY();
        } else if(lockedInObject.equals("Rocket")) {
            xCenter = WIDTH / 2 - objects.get(objects.size()-1).getCurrentX();
            yCenter = HEIGHT / 2 - objects.get(objects.size()-1).getCurrentY();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

