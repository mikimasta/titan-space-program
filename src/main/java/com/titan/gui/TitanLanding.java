package com.titan.gui;

import com.titan.LandingSimulation;
import com.titan.controls.FirstLandingControls;
import com.titan.controls.LandingControls;
import com.titan.math.solver.RungeKuttaSolver;
import com.titan.model.LandingModule;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitanLanding extends Application {

    public static boolean landingFinished = false;

    public static double scale = 0.3;
    public final static int WIDTH = 1400;
    public final static int HEIGHT = 800;
    public final static int X_CENTER = 700;
    public final static int Y_CENTER = 750;
    boolean running = false;

    private LandingSimulation simulation;

    private LandingModule landingModule;

    private LandingModuleGUI module;

    private LandingModuleDetailsGUI detailsGUI;

    private Scene prevScene;
    private int currentStep = 0;


    public TitanLanding() {

        landingModule = new LandingModule("Landing Module");
        module = new LandingModuleGUI(landingModule);
        detailsGUI = new LandingModuleDetailsGUI(landingModule);
        LandingControls controls = new FirstLandingControls();
        simulation = new LandingSimulation(new RungeKuttaSolver(), 1, landingModule, controls);

    }
    public void setPreviousScene(Scene s) {
        prevScene = s;
    }

    public Scene getScene()  {

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);


        root.setBackground(new Background(Images.BACKGROUND_IMAGE));
        root.getStylesheets().add(("styling.css"));


        root.getChildren().add(module);
        root.getChildren().add(detailsGUI);
        XAxisGUI xAxis = new XAxisGUI();
        root.getChildren().add(xAxis);

        module.updatePosition();
        module.repaint();
        // System.out.println(module.getCurrentX());
        // System.out.println(module.getCurrentY());

        Button exitLanding = new Button("Exit landing");
        exitLanding.setLayoutX(WIDTH - 180);
        exitLanding.setLayoutY(20);
        exitLanding.setFocusTraversable(false);
        exitLanding.setOnAction(e -> {

            Titan.gameWindow.setScene(prevScene);


        });

        root.getChildren().add(exitLanding);

        Circle origin = new Circle();
        origin.setFill(Color.TRANSPARENT);
        origin.setStroke(Color.SILVER);
        origin.setCenterX(X_CENTER);
        origin.setCenterY(Y_CENTER);
        origin.setRadius(2);

        root.getChildren().addAll(origin);

        KeyFrame kf = new KeyFrame(Duration.millis(5), e -> {
            if (running) {

                for (int i = 0; i < 1; i++) {
                    currentStep++;
                    simulation.nextStep(currentStep);
                    module.updatePosition();
                    module.repaint();
                }

                if (landingModule.getY()<=0) {
                    running = false;
                    System.out.println("Landed at x = " + landingModule.getX() + ", y = " + landingModule.getY());
                }
            }
            detailsGUI.repaint();
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                running = !running;
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                currentStep++;
                simulation.nextStep(currentStep);
                module.updatePosition();
                module.repaint();
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, e -> {
            double delta = (scale / 10) * (e.getDeltaY() / 32);
            double newScale = scale - delta;
            if(newScale > 0.0001 && newScale < 2) scale = newScale;
            module.repaint();
            xAxis.updateScale();
        });

        Timeline tl = new Timeline(kf);
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
