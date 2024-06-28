package com.secui.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortalResponseDto {


    private String portalName;

    private String shortName;

    private String supportEmail;

    private String whatsAppNo;

    private String address;

    private String domainName;

    private String copyright;

    private String disclaimer;

    private String logoUrl;

    private MultipartFile logo;

    private String facebookUrl;

    private String linkedinUrl;

    private String twitterUrl;

    private String youtubeUrl;

    private String instagramUrl;

    private String trustPilotUrl;
    private String status;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Date createdDate;
}
