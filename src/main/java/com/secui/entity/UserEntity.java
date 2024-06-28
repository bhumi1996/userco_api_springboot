package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = TableUtility.USER_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends Auditable implements Serializable {

    @Column(name = ColumnUtils.FULL_NAME)
    private String fullName;
    @Column(name = ColumnUtils.EMAIL)
    private String email;
    @Column(name = ColumnUtils.PASSWORD)
    private String password;
    @Column(name = ColumnUtils.COUNTRY_DIAL)
    private String countryDial;
    @Column(name = ColumnUtils.MOBILE_NUMBER)
    private String mobileNo;
    @Column(name = ColumnUtils.PROFILE_PATH)
    private String profilePath;
    @Column(name = ColumnUtils.ENABLED)
    private boolean enabled;

    @Column(name = ColumnUtils.OTP)
    private String otp;

    @Column(name = ColumnUtils.TOKEN_EXPIRED)
    private boolean tokenExpired;

    @Column(name = ColumnUtils.ACC_LOCKED)
    private boolean accountLocked;

    @Column(name = ColumnUtils.FAILED_ATTEMPT, columnDefinition = "integer default 0")
    private Integer failedAttempt;

    @Column(name = ColumnUtils.LOCT_TIME)
    private Date lockTime;

    @Column(name = ColumnUtils.LOGGED)
    private boolean logged;

    @Column(name = ColumnUtils.ACC_NON_EXP)
    private boolean accountNonExpired;

    @Column(name = ColumnUtils.ACCESS_TOKEN)
    private String accessToken;

    @Column(name = ColumnUtils.TC)
    private String tc;


    @Column(name = ColumnUtils.CREDENTIAL_NON_EXP)
    private boolean credentialsNonExpired;

    private boolean mfaEnabled; // flag to indicate of mfa is active for profile
    private String secret;


    @Column(name = ColumnUtils.PROFILE_IMAGE_URL)
    private String profileImageUrl;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = TableUtility.USER_ROLES,
        joinColumns  = @JoinColumn(name = ColumnUtils.USER_ID,referencedColumnName = ColumnUtils.PID),
        inverseJoinColumns= @JoinColumn(name = ColumnUtils.ROLE_ID,referencedColumnName = ColumnUtils.PID))
    private Set<RoleEntity> roles = new LinkedHashSet<>();
}
