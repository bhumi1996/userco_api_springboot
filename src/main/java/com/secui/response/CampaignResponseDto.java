package com.secui.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CampaignResponseDto {
    String name;
    String type;
    String status;
    String uKey;
    String createdBy;
    Date createdDate;
    String lastModifiedBy;
    Date lastModifiedDate;
}
