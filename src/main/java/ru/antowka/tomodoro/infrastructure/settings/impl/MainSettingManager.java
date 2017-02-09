package ru.antowka.tomodoro.infrastructure.settings.impl;

import ru.antowka.tomodoro.helper.XmlHandler;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.MainSettings;

/**
 * Manager for traffic snuffer
 */
public class MainSettingManager implements SettingManager<MainSettings> {

    private static final String SETTING_FILE_NAME = "main-settings.xml";

    private static MainSettings settings;

    private XmlHandler<MainSettings> xmlHandler =
            new XmlHandler<>(SETTING_FILE_NAME, MainSettings.class);

    @Override
    public MainSettings loadSettings() {
        if(settings == null) {
            settings = xmlHandler.loadDataFromFile();
        }
        return settings;
    }

    @Override
    public void saveSettings(MainSettings setting) {
        settings = setting;
        xmlHandler.saveDataToFile(settings);
    }

    @Override
    public void refresh() {
        settings = xmlHandler.loadDataFromFile();
    }
}
