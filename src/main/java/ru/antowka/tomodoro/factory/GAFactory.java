package ru.antowka.tomodoro.factory;

import ru.antowka.tomodoro.infrastructure.GoogleAnalyticsTracking;


/**
 * Factory for Google analytics
 */
public class GAFactory implements Factory<GoogleAnalyticsTracking>{

    private static GAFactory instance;
    private GoogleAnalyticsTracking product;

    public static GAFactory getInstance() {

        if(instance == null) {
            instance = new GAFactory();
            instance.product = new GoogleAnalyticsTracking("UA-87817427-1", "555");
        }

        return instance;
    }

    @Override
    public GoogleAnalyticsTracking getInstanceProduct() {
        return product;
    }
}
