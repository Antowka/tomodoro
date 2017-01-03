package ru.antowka.tomodoro.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.antowka.tomodoro.factory.GAFactory;
import ru.antowka.tomodoro.factory.ResourcesFactory;
import ru.antowka.tomodoro.factory.TrafficSnifferFactory;
import ru.antowka.tomodoro.infrastructure.GoogleAnalyticsTracking;
import ru.antowka.tomodoro.infrastructure.Resources;
import ru.antowka.tomodoro.infrastructure.TrafficSniffer;
import ru.antowka.tomodoro.infrastructure.settings.impl.TrafficSnifferSettingManager;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Controller {

    private Stage primaryStage;

    @FXML
    private ImageView logoImg;

    @FXML
    private Text stringTimer;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnPause;

    @FXML
    private Button btnReset;

    @FXML
    private MenuItem trafficSnifferSettings;

    /**
     * Timer
     */
    private Timer timer;

    /**
     * time period in sec (25min)
     */
    private final int timePeriod = 25 * 60;

    /**
     * current timer counter
     */
    private Integer currentTimer = timePeriod;

    /**
     * length for time tick(1000 msec)
     */
    private final int timeTick = 1*1000;

    /**
     * Traffic validator
     */
    private TrafficSniffer trafficSniffer;

    /**
     * Resource for application
     */
    private Resources resources;

    /**
     * Google analytics
     */
    private GoogleAnalyticsTracking ga;

    /**
     * Controller for menu
     */
    private TrafficSnifferController menuController;


    public void initialize(Stage primaryStage) {

        //sounds
        resources = ResourcesFactory
                .getInstance()
                .getInstanceProduct();

        //GA
        ga = GAFactory
                .getInstance()
                .getInstanceProduct();

        //Traffic Control
        trafficSniffer  = TrafficSnifferFactory
                .getInstance()
                .getInstanceProduct();


        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);

        logoImg.setImage(new Image("images/tomato.jpg"));

        stringTimer.setText(timeToString());

        //Main buttons
        btnStart.setOnAction((event) -> onClickStart());
        btnPause.setOnAction((event) -> onClickPause());
        btnReset.setOnAction((event) -> onClickReset());

        //Settings
        trafficSnifferSettings.setOnAction((event -> showTrafficSnifferSettings()));

        initTimer();

        //send event to google analytics
        ga.trackAction("event", "application", "start");
    }

    private void onClickStart() {

        if(currentTimer < 1) {
            currentTimer = timePeriod;
        }
        timer.start();

        startTrafficSniffer();
    }

    private void onClickPause() {
        timer.stop();
        trafficSniffer.disable();
    }

    private void onClickReset() {
        timer.stop();
        currentTimer = timePeriod;
        stringTimer.setText(timeToString());
        trafficSniffer.disable();

    }

    /**
     * Show dialog window for traffic sniffer setting
     */
    private void showTrafficSnifferSettings() {

        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/trafficSnifferSettings.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set properties into the controller
            TrafficSnifferController controller = loader.getController();
            controller.initialize(dialogStage, new TrafficSnifferSettingManager());

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }


    /**
     * Start traffic sniffer tread
     */
    private void startTrafficSniffer() {

        if(trafficSniffer.getState() == Thread.State.NEW) {

            trafficSniffer.enable();
            trafficSniffer.start();

        } else if(trafficSniffer.getState() == Thread.State.WAITING) {

            synchronized (trafficSniffer) {
                trafficSniffer.enable();
                trafficSniffer.notifyAll();
            }
        }
    }

    /**
     * Initialize Timer thread
     */
    private void initTimer() {

        timer = new Timer(timeTick, e -> {
            currentTimer--;
            stringTimer.setText(timeToString());

            if(currentTimer == 0) {
                Platform.runLater(() -> primaryStage.requestFocus());
                timer.stop();
                trafficSniffer.disable();
                resources.playSoundDong();
            }
        });
    }

    /**
     * Convert timestamp to real time
     *
     * @return
     */
    private String timeToString() {

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(currentTimer * 1000),
                TimeUnit.MILLISECONDS.toSeconds(currentTimer * 1000) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTimer * 1000))
        );
    }
}
