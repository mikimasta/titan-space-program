package com.titan.gui;


import com.titan.LandingSimulation;
import com.titan.gui.TextParameter.ParameterType;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitanLanding extends Application {

    public static boolean landingFinished = false;

    public static double scale = 0.3;
    public final static int WIDTH = 1400;
    public final static int HEIGHT = 800;
    public static int xCenter = 700;
    public static int yCenter = 750;
    boolean running = false;

    private LandingSimulation simulation;

    private LandingModule landingModule;

    private LandingModuleGUI module;

    private Scene prevScene;


    public TitanLanding() {
        
        landingModule = new LandingModule("Landing Module");
        module = new LandingModuleGUI(landingModule);
        simulation = new LandingSimulation(new RungeKuttaSolver(), 1, landingModule);

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

   
        TextParameter totalVelocity = new TextParameter("Velocity: ", ParameterType.TOTAL_VELOCITY);
        totalVelocity.setLayoutX(20);
        totalVelocity.setLayoutY(20);

        TextParameter xVelocity = new TextParameter("X Velocity: ", ParameterType.X_VELOCITY);
        xVelocity.setLayoutX(20);
        xVelocity.setLayoutY(40);

        TextParameter yVelocity = new TextParameter("Y Velocity: ", ParameterType.Y_VELOCITY);
        yVelocity.setLayoutX(20);
        yVelocity.setLayoutY(60);

        root.getChildren().addAll(totalVelocity, xVelocity, yVelocity);

        KeyFrame kf = new KeyFrame(Duration.millis(5), e -> {
            if (running) {

                for (int i = 0; i < 1; i++) {
                    simulation.nextStep(i);
                    module.updatePosition();
                    module.repaint();
                }

                totalVelocity.update(landingModule);
                xVelocity.update(landingModule);
                yVelocity.update(landingModule);
            }
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
