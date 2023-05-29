package com.wuav.client.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wuav.client.di.ConfigModule;

public class StartUp {

    private static Injector injector;

    /**
     * Configures the dependency injection
     */
    public static void configure() {
        injector = Guice.createInjector(
                new ConfigModule()
        );
    }

    /**
     * Gets the injector
     *
     * @return the injector
     */
    public static Injector getInjector() {
        return injector;
    }

}

