package com.secui.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailConfigurationResponseDto {

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
    private String status;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
}
