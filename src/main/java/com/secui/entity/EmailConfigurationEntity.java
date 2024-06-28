package com.secui.entity;

import com.secui.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = TableUtility.EMAIL_CONFIG_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfigurationEntity extends Auditable{
    @Column
    private String name;

    @Column
    private String encoding;

    @Column
    private String port;

    @Column
    private String protocol;

    @Column
    private String testConnection;

    @Column
    private String smtpAuth;

    @Column
    private String startTlsEnable;

    @Column
    private String fromMail;

    @Column
    private String replyTo;

    @Column
    private String bcc;

    @Column
    private String cc;

    @Column
    private String hostName;

    @Column
    private String userName;

    @Column
    private String password;
}
