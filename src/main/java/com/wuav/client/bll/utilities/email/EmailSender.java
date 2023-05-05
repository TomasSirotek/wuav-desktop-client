package com.wuav.client.bll.utilities.email;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;

public class EmailSender implements IEmailSender {


    @Override
    public boolean sendEmail(Session session, String toEmail, String subject, String body,boolean attachPdf, File pdfFile) {
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("", false));

            msg.setSubject(subject, "UTF-8");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Add the email body
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html; charset=utf-8");
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
            msg.setContent(multipart);

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");


            Transport.send(msg);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
