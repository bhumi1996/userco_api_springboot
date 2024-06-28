package com.secui.request;

import com.secui.utility.ErrorUtil;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailConfigurationRequestDto {

    private String name;
    private String encoding;
    private String port;
    private String protocol;
    private String testConnection;
    private String smtpAuth;
    private String startTlsEnable;
    private String domain;
    private String domainName;
    private String fromMail;
    private String replyTo;
    private String bcc;
    private String cc;
    private String disclaimer;
    private String copyright;
    private String hostName;
    private String userName;
    private String password;
    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
