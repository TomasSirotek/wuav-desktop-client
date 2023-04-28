package com.event_bar_easv.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.event_bar_easv.di.ConfigModule;

public class StartUp {

    private static Injector injector;

    public static void configure() {
        injector = Guice.createInjector(
                new ConfigModule()
        );
    }

    public static Injector getInjector() {
        return injector;
    }

}
