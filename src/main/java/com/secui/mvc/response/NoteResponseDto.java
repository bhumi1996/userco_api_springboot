package com.secui.mvc.response;

import com.secui.mvc.entity.LeadEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteResponseDto {
    private String heading;
    private String message;
    private LeadEntity leadEntity;
    private String status;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
}
