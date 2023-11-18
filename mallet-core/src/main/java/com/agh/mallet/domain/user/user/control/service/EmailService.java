package com.agh.mallet.domain.user.user.control.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.ejb.Asynchronous;
import jakarta.enterprise.context.Dependent;
import lombok.extern.slf4j.Slf4j;

@Dependent
@Slf4j
public class EmailService {

    private final Mailer mailSender;
    public EmailService(Mailer mailSender) {
        this.mailSender = mailSender;
    }

    @Asynchronous
    public void sendMail(String subject, String recipient, String content) {
            Mail mail = Mail.withHtml(recipient, subject, content);
          //  mailSender.send(mail);
    }

}
