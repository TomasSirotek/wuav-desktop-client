module com.wuav.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
    requires org.mybatis;
    requires org.slf4j;
    requires feign.core;
    requires feign.gson;
    requires com.google.common;
    requires org.apache.pdfbox;
    requires org.apache.fontbox;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires activation;
    requires javax.mail;
    requires MaterialFX;
    requires javafx.web;
    requires com.azure.core;
    requires com.azure.storage.blob;
    requires com.google.gson;
    requires thymeleaf;
    requires net.coobird.thumbnailator;
    requires spring.security.crypto;
    requires sendgrid.java;
    requires google.api.client;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;


    exports com.wuav.client.gui.controllers to  com.google.guice, javafx.fxml, com.google.common,org.apache.pdfbox;
    exports com.wuav.client.bll.helpers;
    exports com.wuav.client.bll.services;
    exports com.wuav.client.bll.utilities;
    exports com.wuav.client to javafx.graphics;
    exports com.wuav.client.dal.repository;
    exports com.wuav.client.dal.interfaces;
    exports com.wuav.client.config;
    exports com.wuav.client.di;
    exports com.wuav.client.gui.controllers.controllerFactory;
    exports com.wuav.client.bll.services.interfaces to com.google.guice,org.apache.pdfbox;
    exports com.wuav.client.gui.models.user to com.google.guice;
    exports com.wuav.client.bll.utilities.engines;
    exports com.wuav.client.gui.models to com.google.guice;
    exports  com.wuav.client.bll.utilities.pdf to com.google.guice,org.apache.pdfbox;
    exports com.wuav.client.cache to com.google.guice;


    opens com.wuav.client.gui.controllers to javafx.fxml, com.google.guice, com.google.common,com.azure.core,org.apache.pdfbox;
    opens com.wuav.client.gui.models to javafx.fxml, com.google.guice, com.google.common,com.azure.core,org.apache.pdfbox;
    opens com.wuav.client to javafx.fxml, com.google.guice, org.slf4j,com.azure.core;
    opens com.wuav.client.gui.controllers.abstractController to com.google.guice, javafx.fxml, com.google.common,com.azure.core;
    opens com.wuav.client.gui.controllers.controllerFactory to com.google.guice, javafx.fxml, com.google.common,com.azure.core;
    opens com.wuav.client.config to com.google.guice, javafx.fxml,com.azure.core;
    opens com.wuav.client.di to com.google.guice, javafx.fxml,com.azure.core;
    opens com.wuav.client.dal.myBatis to org.mybatis, javafx.fxml, org.slf4j,com.azure.core;
    opens com.wuav.client.dal.mappers to org.mybatis, com.google.guice,com.azure.core;
    opens com.wuav.client.bll.services to org.mybatis, javafx.fxml, org.slf4j, com.google.guice,com.azure.core,com.azure.storage.blob,spring.security.crypto;
    opens com.wuav.client.bll.services.interfaces to  javafx.fxml, com.google.guice, com.google.common, org.slf4j,com.azure.storage.blob,spring.security.crypto;
    opens com.wuav.client.gui.models.user to javafx.fxml, com.google.guice, com.google.common, org.slf4j,com.azure.core;
    opens com.wuav.client.dal.repository to org.mybatis,javafx.fxml, com.google.guice, com.google.common, org.slf4j,com.azure.core,com.azure.storage.blob;
    exports com.wuav.client.be.user to javafx.graphics, org.mybatis;
    opens com.wuav.client.be.user to com.google.guice, javafx.fxml, org.mybatis, org.slf4j;
    exports com.wuav.client.bll.utilities.email;
    opens com.wuav.client.bll.utilities.engines.cryptoEngine to com.google.guice;
    opens com.wuav.client.bll.utilities.engines to com.google.guice;
    exports com.wuav.client.be to com.google.guice, javafx.graphics, org.mybatis;
    opens com.wuav.client.be to com.google.common, com.google.guice, javafx.fxml, org.mybatis, org.slf4j;

}