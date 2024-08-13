package com.secui.mvc.serviceimpl;

import com.secui.mvc.service.MailApiServiceInterface;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class MailApiServiceImpl implements MailApiServiceInterface {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int NO_OF_THREADES = 20;

    private final ScheduledExecutorService quickService = Executors.newScheduledThreadPool(NO_OF_THREADES);

    public void sendMail(MimeMessage msg) {
        try {
            quickService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(msg);
                    } catch (Exception e) {
                        logger.error("Exception occur while send a mail : ", e);
                    }
                }
            });
        } catch (RuntimeException ex) {
            logger.error("RuntimeException occur while send a mail : ", ex);
        } catch (Exception ex) {
            logger.error("Exception occur while send a mail : ", ex);
        }
    }
}
