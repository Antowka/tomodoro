package ru.antowka.tomodoro.factory;

/**
 * Created by admin on 02.12.2016.
 */
public interface Factory<PRODUCT> {
    PRODUCT getInstanceProduct();
}
