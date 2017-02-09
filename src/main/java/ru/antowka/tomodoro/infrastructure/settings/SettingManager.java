package ru.antowka.tomodoro.infrastructure.settings;

/**
 * Interface for setting manager
 */
public interface SettingManager<T> {

    T loadSettings();
    void saveSettings(T setting);
    void refresh();
}
