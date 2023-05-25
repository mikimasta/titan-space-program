package com.titan.gui;

import com.titan.Simulation;
import com.titan.controls.Controls;
import com.titan.controls.FlightControlsTwoEngineFiresForLaunch;
import com.titan.controls.FlightControlsTwoEngineFiresForLaunch_Exp;
import com.titan.math.Vector;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;

public class Titan extends Application {

    /**
     * scale we use to represent the solar system on the screen
     */
    public static double scale = 3500000;
    public final static int WIDTH = 1400;
    public final static int HEIGHT = 800;

    public static int xCenter = WIDTH / 2;
    public static int yCenter = HEIGHT / 2;

    private static double mouseX = 0;
    private static double mouseY = 0;

    public static int xCenter_ = WIDTH / 2;
    public static int yCenter_ = HEIGHT / 2;

    private String lockedInObject = "Sun";

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
    public static int stepsAtOnce = 5;
    private static final LocalDate START_DATE = LocalDate.of(2023, 4, 1);

    @Override
    public void start(Stage gameWindow) {
        gameWindow.setTitle("Titan Space Program");
        gameWindow.setWidth(WIDTH);
        gameWindow.setHeight(HEIGHT);


        Pane root = new Pane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setBackground(new Background(Images.backgroundImage));
        root.getStylesheets().add(("styling.css"));


        //Hide names button
        ToggleButton hideNames = new ToggleButton("hide/show names");
        hideNames.setStyle("-fx-font-size: 15px");
        hideNames.setLayoutX(WIDTH - 200);
        hideNames.setLayoutY(20);
        hideNames.setFocusTraversable(false);
        root.getChildren().add(hideNames);


        //resize objects button
        ToggleButton resizePlanets = new ToggleButton("resize planets (prop.)");
        resizePlanets.setStyle("-fx-font-size: 15px");
        resizePlanets.setLayoutX(WIDTH - 200);
        resizePlanets.setLayoutY(60);
        resizePlanets.setFocusTraversable(false);
        root.getChildren().add(resizePlanets);

        //back to center button
        Button backToCenter = new Button("back to center");
        backToCenter.setStyle("-fx-font-size: 15px");
        backToCenter.setLayoutX(WIDTH - 200);
        backToCenter.setLayoutY(100);
        backToCenter.setFocusTraversable(false);
        root.getChildren().add(backToCenter);

        //draw orbits button
        ToggleButton drawOrbits = new ToggleButton("draw orbits");
        drawOrbits.setStyle("-fx-font-size: 15px");
        drawOrbits.setLayoutX(WIDTH - 200);
        drawOrbits.setLayoutY(140);
        drawOrbits.setFocusTraversable(false);
        root.getChildren().add(drawOrbits);

        //center to titan button
        ToggleButton centerTitan = new ToggleButton("center on titan");
        centerTitan.setStyle("-fx-font-size: 15px");
        centerTitan.setLayoutX(WIDTH - 200);
        centerTitan.setLayoutY(180);
        centerTitan.setFocusTraversable(false);
        root.getChildren().add(centerTitan);

        //center to earth button
        ToggleButton centerEarth = new ToggleButton("center on earth");
        centerEarth.setStyle("-fx-font-size: 15px");
        centerEarth.setLayoutX(WIDTH - 200);
        centerEarth.setLayoutY(220);
        centerEarth.setFocusTraversable(false);
        root.getChildren().add(centerEarth);

        //center to rocket button
        ToggleButton centerRocket = new ToggleButton("center on rocket");
        centerRocket.setStyle("-fx-font-size: 15px");
        centerRocket.setLayoutX(WIDTH - 200);
        centerRocket.setLayoutY(260);
        centerRocket.setFocusTraversable(false);
        root.getChildren().add(centerRocket);

        //SolarSystem system = new SolarSystem();
        SolarSystem system2 = new SolarSystem("src/main/resources/initial_conditions.csv");

        //Rocket rocket = system.createRocket("Rocket", 50000);
        //system.stageRocket(rocket);

        //Vector force = new Vector(new double[]{38.65346586, -14.90558291, -1.3535296});
        //force = force.multiplyByScalar(rocket.getM()).multiplyByScalar(1.0/stepSize);
        //rocket.fireEngineWithForce(force, stepSize);

        /////////////////////////////
        SolarSystem system = new SolarSystem("resources/system_after_one_year.csv");
        Rocket rocket = system.createRocketAtPointInSpace(
                "Rocket",
                50000,
                new Vector(new double[]{1.3634377057605958E9, -4.868223085696473E8, -4.557917144852597E7}),
                new Vector(new double[]{7.218058164094522, 11.843061237846946, -0.34333197393536263}));
        system.stageRocket(rocket);

        Solver solver = new RungeKuttaSolver(stepSize);

        Controls controls = new FlightControlsTwoEngineFiresForLaunch_Exp(new Vector(new double[]{-26.457795398309827, -0.27845133282244205, -0.10361644625663757}));
        ///////////////////////////////

        ArrayList<CelestialObjectGUI> objects = new ArrayList<>();
        for(CelestialObject o : system.getCelestialObjects()) {
            CelestialObjectGUI objectGUI = new CelestialObjectGUI(o);
            objects.add(objectGUI);
            root.getChildren().add(objectGUI);
            objectGUI.updatePosition();
        }

        DateGUI date = new DateGUI();
        root.getChildren().add(date);

        ScaleGUI scaleGUI = new ScaleGUI();
        root.getChildren().add(scaleGUI);

        Solver eulerSolver = new EulerSolver(stepSize);
        Solver rungeKuttaSolver = new RungeKuttaSolver(stepSize);
        Solver adamsBashforth2 = new AdamsBashforth2ndOrderSolver(stepSize);

        Controls controls3 = new FlightControlsTwoEngineFiresForLaunch();

        Simulation simulation = new Simulation(solver, stepSize, controls, system, rocket);



        KeyFrame kf = new KeyFrame(Duration.millis(0.1), e -> {
            if (running) {
                for (int i = 0; i < stepsAtOnce; i++) {
                    simulation.nextStep(currentStep);
                    currentStep++;
                    if (Titan.currentStep == 365 * 24 * 60 + 1) {
                        Titan.running = false;
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

