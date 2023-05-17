package com.wuav.client.bll.utilities.email;

import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Properties;
import java.util.Set;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;

public class EmailSender implements IEmailSender {


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT,GsonFactory gsonFactory)
            throws IOException {

        String clientSecretFilePath = System.getenv("CONFIG_CLIENT_SECRET");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(gsonFactory,
                        new InputStreamReader(EmailSender.class.getResourceAsStream(clientSecretFilePath)));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, gsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    @Override
    public boolean sendEmail(String toEmail, String subject, String body, boolean attachPdf, File pdfFile) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT,jsonFactory))
                .setApplicationName("Test mailer")
                .build();



        try {
            // Build the email with the provided details.
            MimeMessage mimeMessage = createEmailWithAttachment(toEmail, "me", subject, body, attachPdf, pdfFile);

            // Convert MimeMessage to Gmail's Message.
            Message message = createMessageWithEmail(mimeMessage);

            // Send the email.
            service.users().messages().send("me", message).execute();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, boolean attachPdf, File pdfFile) throws MessagingException, IOException, MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Create a multipart message
        MimeMultipart multipart = new MimeMultipart();

        // Add the email body
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(bodyText, "text/html; charset=utf-8");
        multipart.addBodyPart(bodyPart);

        // Attach the PDF file if necessary
        if (attachPdf && pdfFile != null) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(pdfFile);
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(pdfFile.getName());
            multipart.addBodyPart(attachmentPart);
        }

        // Set the email content
        email.setContent(multipart);

        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }



}

