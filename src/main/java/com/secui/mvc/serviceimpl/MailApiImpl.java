package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.request.LeadMailRequestDto;
import com.secui.mvc.response.EmailConfigurationResponseDto;
import com.secui.mvc.response.EmailTemplateResponseDto;
import com.secui.mvc.service.EmailConfigurationInterface;
import com.secui.mvc.service.EmailTemplateInterface;
import com.secui.mvc.service.MailApiInterface;
import com.secui.mvc.service.MailApiServiceInterface;
import com.secui.mvc.utility.CommonPropertiesUtil;
import com.secui.mvc.utility.ConstantUtil;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailApiImpl implements MailApiInterface {

    final MailApiServiceInterface mailApiServiceInterface;
    final EmailConfigurationInterface emailConfigurationInterface;
    final EmailTemplateInterface emailTemplateInterface;
    final CommonPropertiesUtil commonPropertiesUtil;

    @Override
    public void sendLeadMail(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity) {
        try {
            if (leadEntity.getEmail() != null) {
                MimeMessage msg = getLeadMsg(leadMailRequestDto, leadEntity);
                if (msg != null) {
                    mailApiServiceInterface.sendMail(msg);
                }
            } else {
                log.error("send Lead Mail is not found");
            }
        } catch (Exception ex) {
            log.error("Exception is raised from ::sendLeadMail() ::{}", ex.getMessage());
        }
    }

    private MimeMessage getLeadMsg(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity) {
        try {
            EmailConfigurationResponseDto emailConfigurationResponseDto = emailConfigurationInterface.findByPortalName(leadEntity.getPortalName());
            MimeMessage msg = new MimeMessage(getSession(emailConfigurationResponseDto));
            EmailTemplateResponseDto emailTemplateResponseDto = emailTemplateInterface.findByTemplateNameAndStatus(ConstantUtil.LEAD_MAIL, ConstantUtil.ACTIVE);
            String templateBody = emailTemplateResponseDto.getTemplateBody();
            msg.setRecipients(Message.RecipientType.TO, leadEntity.getEmail());
            msg.setRecipients(Message.RecipientType.CC, emailConfigurationResponseDto.getCc());
            msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(emailConfigurationResponseDto.getBcc()));
            msg.setSubject(emailTemplateResponseDto.getTemplateHeading());
            msg.setFrom(new InternetAddress(emailConfigurationResponseDto.getFromMail(),"USER CO"));
            msg.setReplyTo(new Address[]{new InternetAddress(emailConfigurationResponseDto.getReplyTo())});
            System.out.println("name------------------>"+leadEntity.getLeadName());

            Multipart multipart = new MimeMultipart();
            //Message Body Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(getLeadMailContent(templateBody,leadMailRequestDto,leadEntity), ConstantUtil.HTML_TEXT_URL_ENCODING);
            multipart.addBodyPart(messageBodyPart);
            //Attachment Part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            if (leadMailRequestDto.getAttachment() != null && !leadMailRequestDto.getAttachment().isEmpty()) {
                attachmentPart.attachFile(multipartToFile(leadMailRequestDto.getAttachment()));
                multipart.addBodyPart(attachmentPart);
            }
            msg.setContent(multipart);
            return msg;
        } catch (Exception ex) {
            log.error("Exception from MailApiServiceImpl method geLeadMsg() method", ex);
            return null;
        }
    }

    private String getLeadMailContent(String templateBody, LeadMailRequestDto leadMailDto, LeadEntity leadEntity) {
        return templateBody.replace("{{description}}", leadMailDto.getMessage() != null && !leadMailDto.getMessage().isEmpty() ? leadMailDto.getMessage() : StringUtils.EMPTY)
//                .replace("{{subject}}", leadMailDto.getSubject())
                .replace("{{name}}", leadEntity.getLeadName())
                .replace("{{portal}}",leadEntity.getPortalName())
                .replace("{{address}}", "PORTAL ADDRRESS")
                .replace("{{phone}}","+91987654323")
                .replace("{{whatsapp}}", "+9198765432")
                .replace("{{email}}", "userco@support.com");
    }


    public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty(ConstantUtil.EMAIL_FILE_PATH) + ConstantUtil.SLASH + multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    private Session getSession(EmailConfigurationResponseDto emailConfigurationResponseDto) {
        Properties props = System.getProperties();
        props.put(ConstantUtil.MAIL_HOST, emailConfigurationResponseDto.getHostName());
        props.put(ConstantUtil.MAIL_PORT, emailConfigurationResponseDto.getPort());
        props.put(ConstantUtil.MAIL_STARTTLS_ENABLE, emailConfigurationResponseDto.getStartTlsEnable());
        props.put(ConstantUtil.MAIL_SMTP_AUTH, emailConfigurationResponseDto.getSmtpAuth());
        // create Authenticator object to pass in Session.getInstance argument
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfigurationResponseDto.getUserName(), emailConfigurationResponseDto.getPassword());
            }
        });
    }
}
