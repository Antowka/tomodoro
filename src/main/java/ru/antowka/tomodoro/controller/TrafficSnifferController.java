package ru.antowka.tomodoro.controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.BlockedDomain;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

/**
 * Menu Controller
 */
public class TrafficSnifferController {

    @FXML
    private CheckBox enableControl;

    @FXML
    private TableView blockedList;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    private Stage dialogStage;

    private SettingManager<TrafficSnifferSetting> settingManager;

    private ObservableList<BlockedDomain> blockedDomains;

    private TrafficSnifferSetting settings;

    public void initialize(Stage dialogStage, SettingManager<TrafficSnifferSetting> settingManager) {

        this.settingManager = settingManager;
        this.dialogStage = dialogStage;
        dialogStage.setTitle("Traffic Control Settings");

        settings = settingManager.loadSettings();

        //initialize events
        save.setOnAction(event -> saveSettings());
        cancel.setOnAction(event -> cancel());

        blockedDomains = FXCollections.observableList(settings.getBlockedDomains());

        enableControl.setSelected(settings.isEnable());
        enableControl.selectedProperty().addListener((ov, old_val, new_val) -> {
            settings.setEnable(new_val);
        });

        TableColumn firstNameCol = (TableColumn)blockedList.getColumns().get(0);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<BlockedDomain, String>("domain"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn secondNameCol = (TableColumn)blockedList.getColumns().get(1);
        secondNameCol.setCellValueFactory(new PropertyValueFactory<BlockedDomain, CheckBox>("blocked"));

        secondNameCol.setCellFactory(col -> {
            CheckBoxTableCell<BlockedDomain, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(((BlockedDomain)blockedList.getItems().get(index)).isBlocked());
                active.addListener((obs, wasActive, isNowActive) -> {
                    blockedDomains.get(index).setBlocked(isNowActive);
                });
                return active ;
            });
            return cell ;
        });

        blockedList.setItems(blockedDomains);
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
