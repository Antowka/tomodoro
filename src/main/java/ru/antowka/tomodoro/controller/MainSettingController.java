package ru.antowka.tomodoro.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.MainSettings;

/**
 * Created by anton on 09.02.17.
 */
public class MainSettingController {

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private TextField workTime;

    @FXML
    private TextField relaxTime;

    private SettingManager<MainSettings> settingManager;

    private Stage dialogStage;

    private MainSettings settings;

    /**
     * Initialize menu item view
     *
     * @param dialogStage
     * @param settingManager
     */
    public void initialize(Stage dialogStage, SettingManager<MainSettings> settingManager) {

        this.settingManager = settingManager;
        this.dialogStage = dialogStage;
        dialogStage.setTitle("Traffic Control Settings");

        settingManager.refresh();
        settings = settingManager.loadSettings();

        workTime.setText(String.valueOf(settings.getWorkTime()));
        relaxTime.setText(String.valueOf(settings.getRelaxTime()));

        workTime.selectedTextProperty().addListener(((observable, oldValue, newValue) -> {
            settings.setWorkTime(Integer.valueOf(newValue));
        }));

        relaxTime.selectedTextProperty().addListener(((observable, oldValue, newValue) -> {
            settings.setRelaxTime(Integer.valueOf(newValue));
        }));

        //initialize events
        save.setOnAction(event -> saveSettings());
        cancel.setOnAction(event -> cancel());

        //todo: show and observe settings
    }

    /**
     * Save settings to xml and update setting object
     */
    private void saveSettings() {
        settingManager.saveSettings(settings);
    }

    /**
     * Handle cancel button
     */
    private void cancel() {
        dialogStage.close();
    }
}
