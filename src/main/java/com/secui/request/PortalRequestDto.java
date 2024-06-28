package com.secui.request;

import com.secui.utility.ColumnUtils;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortalRequestDto {

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
}
