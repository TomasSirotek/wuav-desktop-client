package com.wuav.client.bll.utilities.email;

import javax.mail.Session;
import java.io.File;

public interface IEmailSender {

    void  sendEmail(Session session, String toEmail, String subject, String body,boolean attachPdf, File pdfFile);
}
