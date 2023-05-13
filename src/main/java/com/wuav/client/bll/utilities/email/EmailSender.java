package com.wuav.client.bll.utilities.email;


import java.io.DataOutputStream;
import java.io.File;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

public class EmailSender implements IEmailSender {


    @Override
    public boolean sendEmail(String toEmail, String subject, String body, boolean attachPdf, File pdfFile) {
        String fromEmail = "no_reply@example.com";
        String fromName = "NoReply-JD";

        JsonObject from = new JsonObject();
        from.addProperty("email", fromEmail);

        JsonObject to = new JsonObject();
        to.addProperty("email", toEmail);
        JsonArray tos = new JsonArray();
        tos.add(to);

        JsonObject personalization = new JsonObject();
        personalization.add("to", tos);
        JsonArray personalizations = new JsonArray();
        personalizations.add(personalization);

        JsonObject content = new JsonObject();
        content.addProperty("type", "text/html");
        content.addProperty("value", body);
        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject mail = new JsonObject();
        mail.add("from", from);
        mail.addProperty("subject", subject);
        mail.add("personalizations", personalizations);
        mail.add("content", contents);

        if (attachPdf && pdfFile != null) {
            Base64.Encoder encoder = Base64.getEncoder();
            try {
                String encodedString = encoder.encodeToString(Files.readAllBytes(pdfFile.toPath()));

                JsonObject attachment = new JsonObject();
                attachment.addProperty("content", encodedString);
                attachment.addProperty("type", "application/pdf");
                attachment.addProperty("filename", pdfFile.getName());
                attachment.addProperty("disposition", "attachment");
                JsonArray attachments = new JsonArray();
                attachments.add(attachment);

                mail.add("attachments", attachments);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + "null");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
                String json = new Gson().toJson(mail);
                output.write(json.getBytes("UTF-8"));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_ACCEPTED || responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println("SendGrid API responded with status code: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    }

