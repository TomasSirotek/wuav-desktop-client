package com.wuav.client.bll.utilities.email;


import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.bll.utilities.engines.EmailEngine;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.bll.utilities.pdf.PdfGenerator;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailSender implements IEmailSender {


    private static final String DESCR_TEST = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Molestie at elementum eu facilisis sed odio. Et malesuada fames ac turpis egestas sed tempus. A cras semper auctor neque vitae tempus quam. Eu consequat ac felis donec et odio pellentesque diam volutpat. Adipiscing vitae proin sagittis nisl rhoncus mattis. Condimentum mattis pellentesque id nibh. Ultrices eros in cursus turpis. Egestas tellus rutrum tellus pellentesque eu tincidunt tortor. Enim nulla aliquet porttitor lacus luctus accumsan. Sed vulputate mi sit amet mauris. Molestie ac feugiat sed lectus vestibulum mattis.";


    public static void main(String[] args) throws IOException {
        IEmailSender emailSender = new EmailSender();
        IEmailEngine emailEngine = new EmailEngine();

        IPdfGenerator pdfGenerator = new PdfGenerator();

                Customer customer = new Customer(1, "Tomasko", "eail@yahoo.com", "dsafsdfsdf", "Private");


        Project project = new Project();
        project.setName("tomas");
        project.setDescription(DESCR_TEST);
        project.setCustomer(customer);




        // convert stream to file

        ByteArrayOutputStream stream = pdfGenerator.generatePdf(null,project,"filename");

// Convert stream to byte array
        byte[] pdfBytes = stream.toByteArray();

// Create a temporary file and write the PDF bytes to it
        File pdfFile = File.createTempFile("temp", ".pdf");
        OutputStream os = new FileOutputStream(pdfFile);
        os.write(pdfBytes);
        os.close();




        // genereate pdf report and send it via email

        // Define the template name and variables
        String templateName = "email-template";
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", "Test");
        templateVariables.put("subject", "TLSEmail Testing Subject");

        // Process the template and generate the email body
        String emailBody = emailEngine.processTemplate(templateName, templateVariables);


        String username = "";
        String password = "";
        String smtpHost = "";
        int smtpPort = 587;


        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.host", "smtp.ethereal.email");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.transport.protocol", "smtp");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        emailSender.sendEmail(session, "", "TLSEmail Testing Subject", emailBody, true, pdfFile);
    }
    @Override
    public void sendEmail(Session session, String toEmail, String subject, String body,boolean attachPdf, File pdfFile) {
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

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
