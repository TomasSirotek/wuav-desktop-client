package com.event_bar_easv.bll.utilities.email;

import javax.mail.Session;

public interface IEmailSender {

    void  sendEmail(Session session, String toEmail, String subject, String body);
}
