package com.secui.mvc.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadResponseDto {
    private  String portalName;
    private String leadName;
    private String email;
    private String dialCode;
    private String mobileNumber;
    private String country;
    private String stage;
    private String channel;
    private String gender;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
}
