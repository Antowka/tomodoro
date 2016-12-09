package ru.antowka.tomodoro.controller;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

/**
 * Menu Controller
 */
public class TrafficSnifferController {

    @FXML
    private CheckBox enableControl;

    private Stage dialogStage;
    private SettingManager<TrafficSnifferSetting> settingManager;

    public void initialize(Stage dialogStage, SettingManager<TrafficSnifferSetting> settingManager) {

        this.settingManager = settingManager;
        this.dialogStage = dialogStage;
        dialogStage.setTitle("Traffic Control Settings");

        TrafficSnifferSetting settings = settingManager.loadSettings();
        enableControl.setSelected(settings.isEnable());
    }
}
