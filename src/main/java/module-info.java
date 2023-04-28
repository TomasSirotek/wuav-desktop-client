module com.event_bar_easv {
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


    exports com.event_bar_easv.gui.controllers to  com.google.guice, javafx.fxml, com.google.common;
    exports com.event_bar_easv.bll.helpers;
    exports com.event_bar_easv.bll.services;
    exports com.event_bar_easv.bll.utilities;
    exports com.event_bar_easv to javafx.graphics;
    exports com.event_bar_easv.dal.reporitory;
    exports com.event_bar_easv.dal.interfaces;
    exports com.event_bar_easv.config;
    exports com.event_bar_easv.di;
    exports com.event_bar_easv.gui.controllers.event;
    exports com.event_bar_easv.gui.controllers.controllerFactory;
    exports com.event_bar_easv.bll.services.interfaces to com.google.guice;
    exports com.event_bar_easv.gui.models.user to com.google.guice;
    exports com.event_bar_easv.bll.utilities.pdf to org.apache.pdfbox, org.apache.fontbox,com.google.zxing,javase,java.desktop, com.google.guice;
    exports com.event_bar_easv.bll.utilities.engines;

    opens com.event_bar_easv.bll.utilities.pdf to org.apache.pdfbox, org.apache.fontbox,com.google.zxing,javase,java.desktop, com.google.guice;
    opens com.event_bar_easv.gui.controllers to javafx.fxml, com.google.guice, com.google.common;
    opens com.event_bar_easv to javafx.fxml, com.google.guice, org.slf4j;
    opens com.event_bar_easv.gui.controllers.abstractController to com.google.guice, javafx.fxml, com.google.common;
    opens com.event_bar_easv.gui.controllers.controllerFactory to com.google.guice, javafx.fxml, com.google.common;
    opens com.event_bar_easv.config to com.google.guice, javafx.fxml;
    opens com.event_bar_easv.di to com.google.guice, javafx.fxml;
    opens com.event_bar_easv.dal.myBatis to org.mybatis, javafx.fxml, org.slf4j;
    opens com.event_bar_easv.dal.mappers to org.mybatis, com.google.guice;
    opens com.event_bar_easv.bll.services to org.mybatis, javafx.fxml, org.slf4j, com.google.guice;
    opens com.event_bar_easv.bll.services.interfaces to  javafx.fxml, com.google.guice, com.google.common, org.slf4j;
    opens com.event_bar_easv.gui.models.user to javafx.fxml, com.google.guice, com.google.common, org.slf4j;
    opens com.event_bar_easv.dal.reporitory to org.mybatis,javafx.fxml, com.google.guice, com.google.common, org.slf4j;
    exports com.event_bar_easv.be.user to javafx.graphics, org.mybatis;
    opens com.event_bar_easv.be.user to com.google.guice, javafx.fxml, org.mybatis, org.slf4j;
    exports com.event_bar_easv.gui.models.event to com.google.guice, javafx.graphics, org.mybatis;
    opens com.event_bar_easv.gui.models.event to com.google.common, com.google.guice, javafx.fxml, org.slf4j;
    exports com.event_bar_easv.bll.utilities.email;
    opens com.event_bar_easv.bll.utilities.engines to com.google.guice;
    exports com.event_bar_easv.be to com.google.guice, javafx.graphics, org.mybatis;
    opens com.event_bar_easv.be to com.google.common, com.google.guice, javafx.fxml, org.mybatis, org.slf4j;

}