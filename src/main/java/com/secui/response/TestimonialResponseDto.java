package com.secui.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestimonialResponseDto {
    private String portalName;

    private String name;

    private String location;

    private String designation;

    private String comments;

    private String fileName;

    private MultipartFile profileImage;

    private String status;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Date createdDate;

}
