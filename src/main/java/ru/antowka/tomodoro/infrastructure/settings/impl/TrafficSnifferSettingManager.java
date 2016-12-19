package ru.antowka.tomodoro.infrastructure.settings.impl;

import ru.antowka.tomodoro.helper.XmlHandler;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

/**
 * Manager for traffic snuffer
 */
public class TrafficSnifferSettingManager implements SettingManager<TrafficSnifferSetting> {

    private static final String SETTING_FILE_NAME = "traffic-control-setting.xml";

    private static TrafficSnifferSetting settings;

    private XmlHandler<TrafficSnifferSetting> xmlHandler =
            new XmlHandler<>(SETTING_FILE_NAME, TrafficSnifferSetting.class);

    @Override
    public TrafficSnifferSetting loadSettings() {
        if(settings == null) {
            settings = xmlHandler.loadDataFromFile();
        }
        return settings;
    }

    @Override
    public void saveSettings(TrafficSnifferSetting setting) {
        settings = setting;
        xmlHandler.saveDataToFile(settings);
    }
}
