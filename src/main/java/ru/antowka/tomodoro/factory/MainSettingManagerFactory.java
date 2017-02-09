package ru.antowka.tomodoro.factory;

import ru.antowka.tomodoro.infrastructure.settings.impl.MainSettingManager;

/**
 * Main Setting manager
 */
public class MainSettingManagerFactory implements Factory<MainSettingManager> {

    private static MainSettingManagerFactory instance;
    private static MainSettingManager manager;

    public static MainSettingManagerFactory getInstance() {

        if (instance == null) {
            instance = new MainSettingManagerFactory();
        }

        return instance;
    }

    @Override
    public MainSettingManager getInstanceProduct() {

        if (manager == null) {
            manager = new MainSettingManager();
        }

        manager.loadSettings();

        return manager;
    }
}
