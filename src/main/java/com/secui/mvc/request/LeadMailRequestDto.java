package com.secui.mvc.request;

import com.secui.mvc.entity.LeadEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadMailRequestDto {

    String email;
    String name;
    String templateName;
    String subject;
    String message;
    MultipartFile attachment;
    private LeadEntity leadEntity;
}
