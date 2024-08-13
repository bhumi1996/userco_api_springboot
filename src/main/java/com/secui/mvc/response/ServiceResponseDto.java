package com.secui.mvc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceResponseDto {

    private String serviceName;
    private String description;
    private String imageUrl;
    private MultipartFile image;
    private String status;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Date createdDate;
}
