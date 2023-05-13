package com.wuav.client.bll.utilities.email;

import com.sendgrid.SendGrid;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class EmailConnectionFactory {
    private static final String CONFIG_FILE_NAME = "emailConfig.cfg";

    private static Properties props;

    private static SendGrid sendGridInstance;

    private static String apiKey;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            InputStream inputStream = EmailConnectionFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            props = new Properties();
            if (inputStream != null) {
                props.load(inputStream);
                inputStream.close();
            } else {
                throw new RuntimeException("Property file '" + CONFIG_FILE_NAME + "' not found in the classpath");
            }

            apiKey = props.getProperty("apiKey");
            if (apiKey == null) {
                throw new RuntimeException("API key not found in properties file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SendGrid getSession() {
        if (sendGridInstance == null) {
            sendGridInstance = new SendGrid(apiKey);
        }
        return sendGridInstance;
    }
}
