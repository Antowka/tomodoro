package ru.antowka.tomodoro.controller;


import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Menu Controller
 */
public class TrafficSnifferController {

    private Stage dialogStage;

    @FXML
    private void initialize() {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setTitle("Traffic Control Settings");
    }
}
