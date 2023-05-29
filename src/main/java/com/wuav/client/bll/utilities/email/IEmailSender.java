package com.wuav.client.bll.utilities.email;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * The interface for the email sender
 */
public interface IEmailSender {

    /**
     * Sends an email
     *
     * @param toEmail   the email to send to
     * @param subject   the subject of the email
     * @param body      the body of the email
     * @param attachPdf if the pdf should be attached
     * @param pdfFile   the pdf file to attach
     * @return boolean if the email was sent
     * @throws GeneralSecurityException if the credentials are invalid
     * @throws IOException              if the email could not be sent
     */
    boolean sendEmail(String toEmail, String subject, String body, boolean attachPdf, File pdfFile) throws GeneralSecurityException, IOException;
}
