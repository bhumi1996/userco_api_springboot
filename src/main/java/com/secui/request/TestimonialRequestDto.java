package com.secui.request;

import com.secui.utility.ColumnUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestimonialRequestDto {

    private String portalName;

    private String name;

    private String location;

    private String designation;

    private String comments;

    private String fileName;

    private MultipartFile profileImage;

    private String status;
}
