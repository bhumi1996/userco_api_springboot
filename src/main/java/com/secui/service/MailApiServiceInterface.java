package com.secui.service;

import jakarta.mail.internet.MimeMessage;

public interface MailApiServiceInterface {
    void sendMail(MimeMessage msg);
}
