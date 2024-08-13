package com.secui.mvc.request;

import com.secui.mvc.annotation.EmailValidation;
import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailConfigurationRequestDto {

    @NotNull(message = ErrorUtil.LEAD_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String portalName;
    @NotNull(message = ErrorUtil.NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String name;
    @NotNull(message = ErrorUtil.ENCODING_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String encoding;
    @NotNull(message = ErrorUtil.PORT_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String port;
    @NotNull(message = ErrorUtil.PROTOCOL_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String protocol;
    @NotNull(message = ErrorUtil.SMTP_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String smtpAuth;
    @NotNull(message = ErrorUtil.TLS_ENABLE_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String startTlsEnable;
    @NotNull(message = ErrorUtil.FROM_MAIL_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    @EmailValidation
    private String fromMail;
    @NotNull(message = ErrorUtil.REPLY_TO_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    @EmailValidation
    private String replyTo;
    @NotNull(message = ErrorUtil.BCC_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    @EmailValidation
    private String bcc;
    @NotNull(message = ErrorUtil.CC_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    @EmailValidation
    private String cc;
    @NotNull(message = ErrorUtil.HOST_NAME_REQUITRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String hostName;
    @NotNull(message = ErrorUtil.USERNAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String userName;
    @NotNull(message = ErrorUtil.PASSWORD_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String password;
    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
