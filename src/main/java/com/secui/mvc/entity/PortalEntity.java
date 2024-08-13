package com.secui.mvc.entity;

import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.PORTAL_MSTR_TBL)
public class PortalEntity extends Auditable{

    @Column(name= ColumnUtils.PORTAL_NAME)
    private String portalName;

    @Column(name=ColumnUtils.SHORT_NAME)
    private String shortName;

    @Column(name=ColumnUtils.SUPPORT_EMAIL)
    private String supportEmail;


    @Column(name=ColumnUtils.WHATS_APP_NO)
    private String whatsAppNo;


    @Column(name=ColumnUtils.ADDRESS,columnDefinition = ColumnUtils.LONG_TEXT)
    private String address;

    @Column(name= ColumnUtils.DOMAIN_NAME)
    private String domainName;

    @Column(name=ColumnUtils.COPYRIGHT,columnDefinition = ColumnUtils.TEXT)
    private String copyright;

    @Column(name=ColumnUtils.DISCLAIMER,columnDefinition = ColumnUtils.TEXT)
    private String disclaimer;

    @Column(name=ColumnUtils.LOGO_URL,columnDefinition = ColumnUtils.TEXT)
    private String logoUrl;

    @Column(name = ColumnUtils.FACEBOOK_URL,columnDefinition = ColumnUtils.TEXT)
    private String facebookUrl;

    @Column(name = ColumnUtils.LINKEDIN_URL,columnDefinition = ColumnUtils.TEXT)
    private String linkedinUrl;

    @Column(name = ColumnUtils.TWITTER_URL,columnDefinition = ColumnUtils.TEXT)
    private String twitterUrl;

    @Column(name = ColumnUtils.YOUTUBE_URL,columnDefinition = ColumnUtils.TEXT)
    private String youtubeUrl;

    @Column(name = ColumnUtils.INSTAGRAM_URL,columnDefinition = ColumnUtils.TEXT)
    private String instagramUrl;

    @Column(name = ColumnUtils.TRUSTPILOT_URL,columnDefinition = ColumnUtils.TEXT)
    private String trustPilotUrl;

}
