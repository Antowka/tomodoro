package ru.antowka.tomodoro.controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.BlockedDomain;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.pcap4j.core.Pcaps.findAllDevs;

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

    @FXML
    private ChoiceBox<String> ethInterface;

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


        //Enable\Disable flag
        enableControl.setSelected(settings.isEnable());
        enableControl.selectedProperty().addListener((ov, old_val, new_val) -> {
            settings.setEnable(new_val);
        });

        //Blocked domain list
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

        //Choice eth interface

        List<String> interfaces  = new ArrayList<>();

        try {

            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
                    .flatMap(i -> Collections.list(i.getInetAddresses()).stream())
                    .filter(ip -> ip instanceof Inet4Address)
                    .map(InetAddress::getHostName)
                    .collect(Collectors.toList());

        } catch (SocketException e) {
            e.printStackTrace();
        }

        ethInterface.setTooltip(new Tooltip("Select the use IP"));
        ethInterface.setItems(FXCollections.observableArrayList(interfaces));
        ethInterface.getSelectionModel().select(settings.getInterfaceName());
        ethInterface.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                settings.setInterfaceName(newValue));

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
