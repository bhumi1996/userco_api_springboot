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
public class ServiceRequestDto {
    private String serviceName;
    private String description;
    private String imageUrl;
    private MultipartFile image;
    private String status;
}
