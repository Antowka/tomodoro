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
import ru.antowka.tomodoro.factory.MainSettingManagerFactory;
import ru.antowka.tomodoro.factory.ResourcesFactory;
import ru.antowka.tomodoro.infrastructure.GoogleAnalyticsTracking;
import ru.antowka.tomodoro.infrastructure.Resources;
import ru.antowka.tomodoro.infrastructure.settings.impl.MainSettingManager;

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
    private MenuItem mainSettings;

    /**
     * Timer
     */
    private Timer timer;

    /**
     * time period in sec
     */
    private int timePeriod;

    /**
     * current timer counter
     */
    private Integer currentTimer = timePeriod;

    /**
     * length for time tick(1000 msec)
     */
    private final int timeTick = 1 * 1000;

    /**
     * Resource for application
     */
    private Resources resources;

    /**
     * Google analytics
     */
    private GoogleAnalyticsTracking ga;

    /**
     * Setting manager
     */
    private MainSettingManager mainSettingManager;


    public void initialize(Stage primaryStage) {

        //sounds
        resources = ResourcesFactory
                .getInstance()
                .getInstanceProduct();

        //GA
        ga = GAFactory
                .getInstance()
                .getInstanceProduct();

        //setting manager
        mainSettingManager = MainSettingManagerFactory
                .getInstance()
                .getInstanceProduct();

        currentTimer = timePeriod = mainSettingManager
                .loadSettings()
                .getWorkTime() * 60;


        this.primaryStage = primaryStage;

        logoImg.setImage(new Image("images/tomato.jpg"));

        stringTimer.setText(timeToString());

        //Main buttons
        btnStart.setOnAction((event) -> onClickStart());
        btnPause.setOnAction((event) -> onClickPause());
        btnReset.setOnAction((event) -> onClickReset());

        //Settings
        mainSettings.setOnAction((event -> showMainSettings()));

        initTimer();

        //send event to google analytics
        ga.trackAction("event", "application", "start");

        primaryStage.setOnCloseRequest(e -> {
            if (timer != null) {
                timer.stop();
            }
        });
    }

    private void onClickStart() {

        currentTimer = timePeriod = mainSettingManager
                .loadSettings()
                .getWorkTime() * 60;

        if (currentTimer < 1) {
            currentTimer = timePeriod;
        }
        timer.start();
    }

    private void onClickPause() {
        timer.stop();
    }

    private void onClickReset() {
        timer.stop();
        currentTimer = timePeriod;
        stringTimer.setText(timeToString());
    }

    /**
     * Show dialog window for traffic sniffer setting
     */
    private void showMainSettings() {

        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainSettings.fxml"));
            AnchorPane page = loader.load();
            MainSettingController mainSettingController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Main settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            mainSettingController.initialize(dialogStage, mainSettingManager);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Initialize Timer thread
     */
    private void initTimer() {

        timer = new Timer(timeTick, e -> {
            currentTimer--;
            stringTimer.setText(timeToString());

            if (currentTimer == 0) {
                Platform.runLater(() -> primaryStage.requestFocus());
                timer.stop();
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
