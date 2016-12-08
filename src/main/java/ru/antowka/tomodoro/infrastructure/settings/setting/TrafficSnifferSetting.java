package ru.antowka.tomodoro.infrastructure.settings.setting;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings for TrafficSniffer
 */
@Data
public class TrafficSnifferSetting {

    private static final String settingFileName = "traffic-control-setting.xml";

    private boolean enable;
    private Map<String, Boolean> blockedDomains = new HashMap<>();
}
