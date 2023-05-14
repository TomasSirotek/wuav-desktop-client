package com.wuav.client.bll.utilities.email;

import javax.mail.Session;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IEmailSender {

    boolean sendEmail(String toEmail, String subject, String body, boolean attachPdf, File pdfFile) throws GeneralSecurityException, IOException;
}
