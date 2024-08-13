package com.secui.mvc.request;

import com.secui.mvc.annotation.EmailValidation;
import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortalRequestDto {

    @NotNull(message = ErrorUtil.PORTAL_NAME_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.PORTAL_LENGTH_VALIDATION)
    private String portalName;


    @NotNull(message = ErrorUtil.SHORT_NAME_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.PORTAL_LENGTH_VALIDATION)
    private String shortName;


    @NotNull(message = ErrorUtil.EMAIL_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.EMAIL_LENGTH_VALIDATION)
    @EmailValidation
    private String supportEmail;

    @NotNull(message = ErrorUtil.CONTACT_INFO_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.CONTACT_LENGTH_VALIDATION)
    private String whatsAppNo;


    @NotNull(message = ErrorUtil.ADDRESS_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.ADDRESS_LENGTH_VALIDATION)
    private String address;

    @NotNull(message = ErrorUtil.DOMAIN_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.DOMAIN_LENGTH_VALIDATION)
    private String domainName;


    @NotNull(message = ErrorUtil.COPYRIGHT_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.MIN_MAX_LENGTH)
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

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
