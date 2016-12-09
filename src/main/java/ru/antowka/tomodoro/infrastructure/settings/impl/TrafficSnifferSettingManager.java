package ru.antowka.tomodoro.infrastructure.settings.impl;

import ru.antowka.tomodoro.helper.XmlHandler;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

/**
 * Manager for traffic snuffer
 */
public class TrafficSnifferSettingManager implements SettingManager<TrafficSnifferSetting> {

    private static final String SETTING_FILE_NAME = "traffic-control-setting.xml";

    private XmlHandler<TrafficSnifferSetting> xmlHandler =
            new XmlHandler<>(SETTING_FILE_NAME, TrafficSnifferSetting.class);

    @Override
    public TrafficSnifferSetting loadSettings() {
        return xmlHandler.loadDataFromFile();
    }

    @Override
    public void saveSettings(TrafficSnifferSetting setting) {
        xmlHandler.saveDataToFile(setting);
    }
}
