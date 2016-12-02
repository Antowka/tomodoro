package ru.antowka.tomodoro;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.antowka.tomodoro.factory.GAFactory;
import ru.antowka.tomodoro.factory.ResourcesFactory;
import ru.antowka.tomodoro.factory.TrafficSnifferFactory;
import ru.antowka.tomodoro.infrastructure.GoogleAnalyticsTracking;
import ru.antowka.tomodoro.infrastructure.Resources;
import ru.antowka.tomodoro.infrastructure.TrafficSniffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private TrafficSniffer trafficSniffer  = TrafficSnifferFactory
            .getInstance()
            .getInstanceProduct();

    /**
     * Resource for application
     */
    private Resources resources = ResourcesFactory
            .getInstance()
            .getInstanceProduct();

    private GoogleAnalyticsTracking ga = GAFactory
            .getInstance()
            .getInstanceProduct();


    public void initialize(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);

        logoImg.setImage(new Image("images/tomato.jpg"));

        stringTimer.setText(timeToString());
        btnStart.setOnAction((event) -> onClickStart());
        btnPause.setOnAction((event) -> onClickPause());
        btnReset.setOnAction((event) -> onClickReset());

        initTimer();

        //send event to google analytics
        ga.trackAction("event", "application", "start");
    }

    private void onClickStart() {

        if(currentTimer < 1) {
            currentTimer = timePeriod;
        }
        timer.start();

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

    private void initTimer() {

        timer = new Timer(timeTick, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTimer--;
                stringTimer.setText(timeToString());

                if(currentTimer == 0) {
                    Platform.runLater(() -> primaryStage.requestFocus());
                    timer.stop();
                    trafficSniffer.disable();
                    resources.playSoundDong();
                }
            }
        });
    }

    private String timeToString() {

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(currentTimer * 1000),
                TimeUnit.MILLISECONDS.toSeconds(currentTimer * 1000) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTimer * 1000))
        );
    }
}
