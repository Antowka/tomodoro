package ru.antowka.tomodoro.controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

    private Stage dialogStage;
    private SettingManager<TrafficSnifferSetting> settingManager;

    public void initialize(Stage dialogStage, SettingManager<TrafficSnifferSetting> settingManager) {

        this.settingManager = settingManager;
        this.dialogStage = dialogStage;
        dialogStage.setTitle("Traffic Control Settings");

        TrafficSnifferSetting settings = settingManager.loadSettings();
        enableControl.setSelected(settings.isEnable());

        TableColumn firstNameCol = (TableColumn)blockedList.getColumns().get(0);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<BlockedDomain, String>("domain"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn secondNameCol = (TableColumn)blockedList.getColumns().get(1);
        secondNameCol.setCellValueFactory(
                new PropertyValueFactory<BlockedDomain, CheckBox>("blocked"));

        secondNameCol.setCellFactory(col -> {
            CheckBoxTableCell<BlockedDomain, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(((BlockedDomain)blockedList.getItems().get(index)).isBlocked());
                active.addListener((obs, wasActive, isNowActive) -> {

                });
                return active ;
            });
            return cell ;
        });

        blockedList.setItems(FXCollections.observableList(settings.getBlockedDomains()));
    }
}
