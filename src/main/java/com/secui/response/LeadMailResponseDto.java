package com.secui.response;

import com.secui.entity.LeadEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadMailResponseDto {

    String email;
    String templateName;
    String subject;
    String message;
    private LeadEntity leadEntity;
    private String status;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

}
