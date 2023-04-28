package com.event_bar_easv.bll.utilities.email;

import com.event_bar_easv.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailConnectionFactory {
    private static final String CONFIG_FILE_NAME = "emailConfig.cfg";
    private static Session session;

    private static Properties props;

    private static Authenticator auth;


    private void loadConfig() {
        try {
            InputStream inputStream = EmailConnectionFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            props = new Properties();
            props.load(inputStream);
            assert inputStream != null;
            inputStream.close();

            auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("EMAIL"), props.getProperty("PASSWORD"));
                }
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static Session getSession() {
        if (session == null) {
            EmailConnectionFactory emailConnectionFactory = new EmailConnectionFactory();
            emailConnectionFactory.loadConfig();
            session = Session.getInstance(props, auth);
        }
        return session;
    }
}
