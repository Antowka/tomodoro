package ru.antowka.tomodoro.factory;

import ru.antowka.tomodoro.infrastructure.Resources;

/**
 * Factory for resources
 */
public class ResourcesFactory implements Factory<Resources>{

    private static ResourcesFactory instance;
    private Resources product;

    public static ResourcesFactory getInstance() {

        if(instance == null) {
            instance = new ResourcesFactory();
            instance.product = new Resources();
        }

        return instance;
    }

    @Override
    public Resources getInstanceProduct() {
        return product;
    }
}
