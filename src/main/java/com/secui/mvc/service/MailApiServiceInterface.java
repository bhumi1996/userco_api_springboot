package com.secui.mvc.service;

import jakarta.mail.internet.MimeMessage;

public interface MailApiServiceInterface {
    void sendMail(MimeMessage msg);
}
